const { Blog, updateBlogValidation, createBlogValidation } = require("../models/blogModel");

const createBlog = async(req, res) => {
    try {
        const { error } = createBlogValidation(req.body);
        if (error) {
            return res.status(400).json({ error: error });
        }

        const blog = new Blog(req.body);

        await blog.save();
        res.status(201).json(blog);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
};

const getAllBlogs = async(req, res) => {
    try {
        const blogs = await Blog.find();
        res.json(blogs);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
};

const getBlogById = async(req, res) => {
    try {
        const blog = await Blog.findById(req.params.blogId);
        if (!blog) {
            return res.status(404).json({ error: 'Blog not found' });
        }
        res.json(blog);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
};

const updateBlogById = async(req, res) => {
    try {
        const { error } = updateBlogValidation(req.body);
        if (error) {
            return res.status(400).json({ error: error.details[0].message });
        }

        const blog = await Blog.findByIdAndUpdate(req.params.blogId, req.body, { new: true });
        if (!blog) {
            return res.status(404).json({ error: 'Blog not found' });
        }
        res.json(blog);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
};

const deleteBlogById = async(req, res) => {
    try {
        const blog = await Blog.findByIdAndDelete(req.params.blogId);
        if (!blog) {
            console.log(blog);
            return res.status(404).json({ error: 'Blog not found' });
        }
        res.json({ message: 'Blog deleted successfully' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
};

module.exports = {
    createBlog,
    getAllBlogs,
    getBlogById,
    updateBlogById,
    deleteBlogById,
};