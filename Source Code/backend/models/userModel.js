const mongoose = require("mongoose");
const Joi = require("joi");
const JoiPassComplex = require("joi-password-complexity");
const Jwt = require("jsonwebtoken");

const UserSchema = new mongoose.Schema(
  {
    username: {
      type: String,
      required: true,
      unique: true,
      trim: true,
      minlength: 4,
      maxlength: 100,
    },
    email: {
      type: String,
      required: true,
      unique: true,
      trim: true,
      minlength: 4,
      maxlength: 256,
    },
    password: {
      type: String,
      required: true,
      trim: true,
      minlength: 8,
      maxlength: 256,
    },
    firstname: {
      type: String,
      trim: true,
      minlength: 3,
      maxlength: 256,
    },
    lastname: {
      type: String,
      trim: true,
      minlength: 3,
      maxlength: 256,
    },
    country: {
      type: String,
      trim: true,
      minlength: 4,
      maxlength: 100,
    },
    rank: {
      type: String,
      default: "unranked",
    },
    friends: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],
    photo: {
      type: Object,
      default: {
        url: "https://images.are.na/eyJidWNrZXQiOiJhcmVuYV9pbWFnZXMiLCJrZXkiOiI4MDQwOTc0L29yaWdpbmFsX2ZmNGYxZjQzZDdiNzJjYzMxZDJlYjViMDgyN2ZmMWFjLnBuZyIsImVkaXRzIjp7InJlc2l6ZSI6eyJ3aWR0aCI6MTIwMCwiaGVpZ2h0IjoxMjAwLCJmaXQiOiJpbnNpZGUiLCJ3aXRob3V0RW5sYXJnZW1lbnQiOnRydWV9LCJ3ZWJwIjp7InF1YWxpdHkiOjkwfSwianBlZyI6eyJxdWFsaXR5Ijo5MH0sInJvdGF0ZSI6bnVsbH19?bc=0",
        publicID: null,
      },
    },
    status: {
      type: Boolean,
      default: false,
    },
    isOnline: {
      type: Boolean,
      default: false,
    },
    // clan: {
    //   type: mongoose.Schema.Types.ObjectId,
    //   ref: "Clan",
    //   default: null,
    // },
  },
  { timestamps: true, toJSON: { virtuals: true }, toObject: { virtuals: true } }
);

/*******************
 * Joining Tables
 *******************/

// Comment Table
UserSchema.virtual("comments", {
  ref: "Comment",
  localField: "_id",
  foreignField: "user",
});
// Match Table
UserSchema.virtual("matches", {
  ref: "Match",
  localField: "_id",
  foreignField: { $in: ["team1", "team2"] },
});
// Submission Table
UserSchema.virtual("submissions", {
  ref: "Submission",
  localField: "_id",
  foreignField: { $in: ["users"] },
});
// Clan Table
////TO DO uncomment when possiable
UserSchema.virtual("clan", {
  ref: "Clan",
  localField: "_id",
  foreignField: { $in: ["members"] },
});

// Generating token
UserSchema.methods.generateAuthToken = function () {
  return Jwt.sign({ id: this._id }, process.env.JWT_SECRETKEY, {
    expiresIn: "30d",
  });
};

// User model
const User = mongoose.model("User", UserSchema);

/*************
 * Validation
 **************/

// Sign Up
const validateSignUp = (user) => {
  const schema = Joi.object({
    username: Joi.string().trim().min(4).max(100).required(),
    email: Joi.string().trim().min(4).max(256).required().email(),
    password: JoiPassComplex().required(),
  });
  return schema.validate(user);
};

// Login
const validateLogin = (user) => {
  const schema = Joi.object({
    email: Joi.string().trim().min(4).max(256).required().email(),
    password: Joi.string().trim().required(),
  });
  return schema.validate(user);
};

module.exports = {
  User,
  validateSignUp,
  validateLogin,
};
