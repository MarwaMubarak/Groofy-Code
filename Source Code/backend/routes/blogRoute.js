const express = require('express');
const router = express.Router();
const blogController = require('../controllers/blogController');

router.post('/blogs/create', blogController.createBlog);
router.get('/blogs', blogController.getAllBlogs);
router.get('/blogs/:blogId', blogController.getBlogById);
router.put('/blogs/update/:blogId', blogController.updateBlogById);
router.delete('/blogs/delete/:blogId', blogController.deleteBlogById);

module.exports = router;