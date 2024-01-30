const router = require('express').Router();
const userController = require('../controllers/UserController/UserController');
const { verifyToken } = require('../middleware/verifyToken');

// Create a new user account
router.post('/register', userController.regiseterUser);

// login
router.post('/login', userController.loginUser);


router.route('/user/update/:userId').put(verifyToken, userController.updateUser);

router.route('/user/:username').get(verifyToken, userController.getUserByUsername);

router.route('/users/:userId').put(verifyToken, userController.changePassword);

router.route('/user/search/:prefix').get(verifyToken, userController.searchUsersByPrefix);


module.exports = router;