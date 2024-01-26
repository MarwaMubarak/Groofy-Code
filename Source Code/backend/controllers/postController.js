const {
  Post,
  updatePostValidation,
  createPostValidation,
} = require("../models/postModel");

/**----------------------------------------
 *  @description  Create New Post
 *  @rounter      /api/post/create
 *  @method       POST
 *  @access       Private (users only)
------------------------------------------*/

const createPost = async (req, res) => {
  // Access the authenticated user through req.user
  const user = req.user;

  try {
    const { error } = createPostValidation(req.body);
    if (error) {
      ret = {
        status: "unsuccess",
        message: error.message,
        data: error._original,
      };
      return res.status(400).json(ret);
    }

    // Create a new post
    const { content } = req.body;
    const post = new Post({
      content,
      user: user._id, // Associate the post with the logged-in user
    });

    await post.save();
    ret = {
      status: "success",
      message: "Post created successfully",
      data: {
        post,
      },
    };
    res.status(201).json(ret);
  } catch (error) {
    console.error(error);
    res.status(500).json({
      status: "unsuccess",
      message: "Internal Server Error",
    });
  }
};

/**----------------------------------------
 *  @description  Get bolg by ID
 *  @rounter      /api/blogs/:blogId
 *  @method       GET
 *  @access       public
------------------------------------------*/
const getBlogById = async (req, res) => {
  try {
    const blog = await Blog.findById(req.params.blogId);
    if (!blog) {
      return res.status(404).json({ error: "Blog not found" });
    }
    res.json(blog);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
  }
};

/**----------------------------------------
 *  @description  Update Blog
 *  @rounter      /api/blogs/update/:blogId
 *  @method       PUT
 *  @access       Private (users only)
------------------------------------------*/
const updatePostById = async (req, res) => {
  try {
    const { error } = updatePostValidation(req.body);
    if (error) {
      return res.status(400).json({ error: error.details[0].message });
    }

    const post = await Post.findByIdAndUpdate(req.params.postId, req.body, {
      new: true,
    });
    if (!post) {
      return res.status(404).json({ error: "Post not found" });
    }

    // Check if the authenticated user has permission to update this blog
    if (String(post.user) !== String(req.user._id)) {
      return res.status(403).json({
        error: "Unauthorized. You do not have permission to update this blog.",
      });
    }

    // Update the blog if the user has permission
    const updatedPost = await post.save();

    res.json(updatedPost);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
  }
};

/**----------------------------------------
 *  @description  Delete Blog
 *  @rounter      /api/blogs/delete/:blogId
 *  @method       DELETE
 *  @access       Private (users only)
-----------------------------------------*/
const deletePostById = async (req, res) => {
  try {
    const post = await Post.findById(req.params.postId);
    if (!post) {
      return res.status(404).json({ error: "Post not found" });
    }
    console.log(post);
    // Check if the authenticated user has permission to delete this blog
    if (String(post.user) !== String(req.user.id)) {
      return res.status(403).json({
        error: "Unauthorized. You do not have permission to delete this blog.",
      });
    }

    // If the user has permission, delete the blog
    await post.deleteOne();

    res.json({ message: "Post deleted successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
  }
};

module.exports = {
  createPost,
  getBlogById,
  updatePostById,
  deletePostById,
};
