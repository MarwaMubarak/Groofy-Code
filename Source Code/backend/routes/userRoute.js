const router = require('express').Router();
const userController = require('../controllers/UserController/UserController');
const userFriendController = require('../controllers/UserController/UserFriendsController');
const { verifyToken } = require('../middleware/verifyToken');

// Create a new user account
router.post('/register', userController.regiseterUser);

// login
router.post('/login', userController.loginUser);

// friend
router.route('/addfriend').post(verifyToken, userFriendController.addFriend);

module.exports = router;