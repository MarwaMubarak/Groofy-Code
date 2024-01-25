<<<<<<< HEAD
const mongoose = require("mongoose");
const Joi = require("joi");

const BlogSchema = new mongoose.Schema(
  {
    title: {
      type: String,
      required: true,
      minlength: 5,
      maxlength: 120,
    },
    content: {
      type: String,
      required: true,
      minlength: 10,
    },
    user: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
    },
    upvotes: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],

    downvotes: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],
  },
  { timestamps: true, toJSON: { virtuals: true }, toObject: { virtuals: true } }
);

// Joining Comments table
BlogSchema.virtual("comments", {
  ref: "Comment",
  localField: "_id",
  foreignField: "blogID",
});

const Blog = mongoose.model("Blog", BlogSchema);

/*************
 * Validation
 **************/

// Create Blog
const createBlogValidation = (blog) => {
  const schema = Joi.object({
    title: Joi.string().min(5).max(120).required(),
    content: Joi.string().min(10).required(),
    user: Joi.string(),
  });
  return schema.validate(blog);
};
// Edit Blog
const updateBlogValidation = (blog) => {
  const schema = Joi.object({
    title: Joi.string().min(5).max(120),
    content: Joi.string().min(10),
    user: Joi.string(),
    upvotes: Joi.array(),
    downvotes: Joi.array(),
  });
  return schema.validate(blog);
};

module.exports = {
  Blog,
  createBlogValidation,
  updateBlogValidation,
};
=======
const mongoose = require("mongoose");
const Joi = require("joi");

const BlogSchema = new mongoose.Schema({
    title: {
        type: String,
        required: true,
        minlength: 5,
        maxlength: 120,
    },
    content: {
        type: String,
        required: true,
        minlength: 10,
    },
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
    },
    upvotes: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
    }, ],


    downvotes: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
    }, ],

}, { timestamps: true, toJSON: { virtuals: true }, toObject: { virtuals: true } });

// Joining Comments table
BlogSchema.virtual("comments", {
    ref: "Comment",
    localField: "_id",
    foreignField: "blogID",
});

const Blog = mongoose.model("Blog", BlogSchema);

/*************
 * Validation
 **************/

// Create Blog
const createBlogValidation = (blog) => {
    const schema = Joi.object({
        title: Joi.string().min(5).max(120).required(),
        content: Joi.string().min(10).required(),
        user: Joi.string(),


    });
    return schema.validate(blog);
};
// Edit Blog
const updateBlogValidation = (blog) => {
    const schema = Joi.object({
        title: Joi.string().min(5).max(120),
        content: Joi.string().min(10),
        user: Joi.string(),
        upvotes: Joi.array(),
        downvotes: Joi.array(),

    });
    return schema.validate(blog);
};

module.exports = {
    Blog,
    createBlogValidation,
    updateBlogValidation,
};
>>>>>>> e9a9b6c849578ccf3b3b8eb842f570e781a73eea
