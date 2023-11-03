const router = require('express').Router();
const userController = require('../controllers/UserController');

// Create a new user account
router.post('/register', userController.regiseterUser);

module.exports = router;
