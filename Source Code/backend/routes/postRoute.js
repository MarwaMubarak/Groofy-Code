const router = require("express").Router();
const postController = require("../controllers/postController/postController");
const { verifyToken } = require('../middleware/verifyToken');

//create Post
router.route('/posts').post(verifyToken, postController.createPost);
router.route('/posts/:postId').put(verifyToken, postController.updatePostById);
router.route('/posts/:postId').delete(verifyToken, postController.deletePostById);
router.route('/posts/:userId').get(verifyToken, postController.getUserPosts);
router.route('/posts/likes/:postId').post(verifyToken, postController.addLike);


module.exports = router;