const express = require('express');
const router = express.Router();
const blogController = require('../controllers/BlogController/blogController');
const { verifyToken } = require('../middleware/verifyToken');


router.route('/blogs').post(verifyToken, blogController.createBlog);
router.get('/blogs', blogController.getAllBlogs);
router.get('/blogs/:blogId', blogController.getBlogById);
router.route('/blogs/:blogId').put(verifyToken, blogController.updateBlogById);
router.route('/blogs/:blogId').delete(verifyToken, blogController.deleteBlogById);

module.exports = router;