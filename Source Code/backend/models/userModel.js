const mongoose = require("mongoose");
const Joi = require("joi");
const JoiPassComplex = require("joi-password-complexity");
const Jwt = require("jsonwebtoken");

const UserSchema = new mongoose.Schema({
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
    city: {
        type: String,
        trim: true,
        minlength: 4,
        maxlength: 100,
    },
    bio: {
        type: String,
        trim: true,
        minlength: 3,
        maxlength: 1000,
    },
    badges: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: "Badge",
    }, ],

    selectedBadges: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: "Badge",
    }, ],
    
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
    totalMatch: {
        type: Number,
        default: 0,
    },
    Trophies: {
        type: Number,
        default: 0,
    },
    wins: {
        type: Number,
        default: 0,
    },
    loses: {
        type: Number,
        default: 0,
    },
    draws: {
        type: Number,
        default: 0,
    },
    division: {
        type: String, // will change
        default: "",
    },
}, {
    timestamps: true,
    toJSON: {
        virtuals: true,
        transform: function(doc, ret) {

            delete ret.id;
            delete ret.__v;

            return ret;
        },
    },
    toObject: { virtuals: true },
});

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
UserSchema.methods.generateAuthToken = function() {
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
        country: Joi.string().trim().min(4).max(100),
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
//Update User
const validateUpdateUser = (user) => {
    const schema = Joi.object({
        firstname: Joi.string().trim().min(3).max(256),
        lastname: Joi.string().trim().min(3).max(256),
        country: Joi.string().trim().min(4).max(100),
        city: Joi.string().trim().min(4).max(100),
        bio: Joi.string().trim().min(4).max(1000),
        friends: Joi.array().items(Joi.string()), // Assuming friends is an array of strings
    });
    return schema.validate(user);
};
const validateChangePassword = (user) => {
    const schema = Joi.object({
        password: JoiPassComplex(),
    });
    return schema.validate(user);
};

module.exports = {
    User,
    validateSignUp,
    validateLogin,
    validateUpdateUser,
    validateChangePassword,
};