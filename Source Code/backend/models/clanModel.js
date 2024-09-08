const mongoose = require("mongoose");
const Joi = require("joi");

const ClanSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      unique: true,
      minlength: 1,
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
      default: "Metal"
    },
  },
  {
    timestamps: true,
    toJSON: {
      virtuals: true,
      transform: function (doc, ret) {
        delete ret.id;
        delete ret.__v;
        delete ret.updatedAt;
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
    name: Joi.string().min(1).max(30).required(),
  });
  return schema.validate(clan);
};
// Edit Clan
const editClanValidation = (clan) => {
  const schema = Joi.object({
    name: Joi.string().min(1).max(30).required(),
  });
  return schema.validate(clan);
};

module.exports = {
  Clan,
  createClanValidation,
  editClanValidation,
};
