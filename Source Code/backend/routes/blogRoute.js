const express = require('express');
const router = express.Router();
const blogController = require('../controllers/blogController');
const { verifyToken } = require('../middleware/verifyToken');


router.route('/blogs/create').post(verifyToken, blogController.createBlog);
router.get('/blogs', blogController.getAllBlogs);
router.get('/blogs/:blogId', blogController.getBlogById);
router.route('/blogs/update/:blogId').put(verifyToken, blogController.updateBlogById);
router.route('/blogs/delete/:blogId').delete(verifyToken, blogController.deleteBlogById);

module.exports = router;