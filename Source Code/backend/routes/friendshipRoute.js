const router = require('express').Router();
const friendshipController = require('../controllers/FriendshipController/FriendshipController');
const { verifyToken } = require('../middleware/verifyToken');


router.route('/addfriend').post(verifyToken, friendshipController.addFriend);

router.route('/removefriend').post(verifyToken, friendshipController.removeFriend);

router.route('/:username/friends').get(verifyToken, friendshipController.getAllFriends);
module.exports = router;