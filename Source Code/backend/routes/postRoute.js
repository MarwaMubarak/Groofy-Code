const router = require("express").Router();
const postController = require("../controllers/postController");
const { verifyToken } = require('../middleware/verifyToken');

//create Post
router.route('/post/create').post(verifyToken, postController.createPost);
router.route('/post/update/:postId').put(verifyToken, postController.updatePostById);
router.route('/post/delete/:postId').delete(verifyToken, postController.deletePostById);
router.get('/posts/:userId', postController.getUserPosts);
router.route('/post/addLike/:postId').post(verifyToken, postController.addLike);


module.exports = router;