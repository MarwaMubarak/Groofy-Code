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
      return res.status(400).json({ error: error });
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
 *  @description  Get all post
 *  @rounter      /api/posts
 *  @method       GET
 *  @access       public
------------------------------------------*/
const getAllPosts = async (req, res) => {
  try {
    const blogs = await Blog.find();
    res.json(blogs);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
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
const updateBlogById = async (req, res) => {
  try {
    const { error } = updateBlogValidation(req.body);
    if (error) {
      return res.status(400).json({ error: error.details[0].message });
    }

    const blog = await Blog.findByIdAndUpdate(req.params.blogId, req.body, {
      new: true,
    });
    if (!blog) {
      return res.status(404).json({ error: "Blog not found" });
    }

    // Check if the authenticated user has permission to update this blog
    if (String(blog.user) !== String(req.user._id)) {
      return res.status(403).json({
        error: "Unauthorized. You do not have permission to update this blog.",
      });
    }

    // Update the blog if the user has permission
    blog.title = req.body.title || blog.title;
    blog.content = req.body.content || blog.content;
    const updatedBlog = await blog.save();

    res.json(updatedBlog);
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
const deleteBlogById = async (req, res) => {
  try {
    const blog = await Blog.findByIdAndDelete(req.params.blogId);
    if (!blog) {
      console.log(blog);
      return res.status(404).json({ error: "Blog not found" });
    }
    // Check if the authenticated user has permission to delete this blog
    if (String(blog.user) !== String(req.user._id)) {
      return res.status(403).json({
        error: "Unauthorized. You do not have permission to delete this blog.",
      });
    }

    // If the user has permission, delete the blog
    await blog.remove();
    res.json({ message: "Blog deleted successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
  }
};

module.exports = {
  createPost,
  getAllBlogs,
  getBlogById,
  updateBlogById,
  deleteBlogById,
};
