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
    createdAt:{
      type: Date,
      default:Date.now
    },
    updatedAt:{
      type: Date,
      default:Date.now

    }
    ,
    likes: [
      {
        user: {
          type: mongoose.Schema.Types.ObjectId,
          ref: "User",
        },
        date: {
          type: Date,
          default: Date.now,
        },
      },
    ],
  },
  {  toJSON: {
    virtuals: true,
    transform: function (doc, ret) {
      // Transform the 'likes' array elements
      ret.likes = ret.likes.map((like) => ({
        date: like.date,
        user: like.user
      }));

      delete ret.id;
      delete ret.__v;

      return ret;
    },
  }, toObject: { virtuals: true } }
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
