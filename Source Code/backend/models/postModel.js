const mongoose = require("mongoose");
const Joi = require("joi");

const PostSchema = new mongoose.Schema(
  {
    user: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
    },
    content: {
      type: String,
      required: true,
      minlength: 1,
    },
    like: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],
  },
  { timestamps: true, toJSON: { virtuals: true }, toObject: { virtuals: true } }
);

const Post = mongoose.model("Post", PostSchema);

/*************
 * Validation
 **************/

// Create Post
const createPostValidation = (post) => {
  const schema = Joi.object({
    content: Joi.string().min(1).required(),
    user: Joi.string(),
  });
  return schema.validate(post);
};
// Edit Post
const updatePostValidation = (post) => {
  const schema = Joi.object({
    content: Joi.string().min(1),
    user: Joi.string(),
    upvotes: Joi.array(),
    downvotes: Joi.array(),
  });
  return schema.validate(post);
};

module.exports = {
  Post,
  createPostValidation,
  updatePostValidation,
};
