const mongoose = require("mongoose");
const Joi = require("joi");

const CommentSchema = new mongoose.Schema(
  {
    blogID: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Blog",
    },
    user: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
    },
    text: {
      type: String,
      required: true,
      minlength: 2,
      maxlength: 200,
    },
  },
  { timestamps: true }
);

const Comment = mongoose.model("Comment", CommentSchema);

/*************
 * Validation
 **************/

// Create Comment
const createCommentValidation = (comment) => {
  const schema = Joi.object({
    text: Joi.string().min(2).max(200).required(),
  });
  return schema.validate(comment);
};
// Edit Comment
const editCommentValidation = (comment) => {
  const schema = Joi.object({
    text: Joi.string().min(2).max(200),
  });
  return schema.validate(comment);
};

module.exports = {
  Comment,
  createCommentValidation,
  editCommentValidation,
};
