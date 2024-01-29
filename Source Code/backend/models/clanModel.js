const mongoose = require("mongoose");
const Joi = require("joi");

const ClanSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      unique: true,
      minlength: 3,
      maxlength: 30,
    },
    leader: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
    },
    members: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],
    rank: {
      type: String,
      required: true,
    },
  },
  {
    timestamps: true,
    toJSON: {
      virtuals: true,
      transform: function (doc, ret) {
        delete ret.id;
        delete ret.__v;

        return ret;
      },
    },
    toObject: { virtuals: true },
  }
);

ClanSchema.virtual("chat", {
  ref: "Chat",
  localField: "_id",
  foreignField: "clanID",
});

const Clan = mongoose.model("Clan", ClanSchema);

/*************
 * Validation
 **************/

// Create Clan
const createClanValidation = (clan) => {
  const schema = Joi.object({
    name: Joi.string().min(3).max(30).required(),
  });
  return schema.validate(clan);
};
// Edit Clan
const editClanValidation = (clan) => {
  const schema = Joi.object({
    name: Joi.string().min(3).max(30),
  });
  return schema.validate(clan);
};

module.exports = {
  Clan,
  createClanValidation,
  editClanValidation,
};
