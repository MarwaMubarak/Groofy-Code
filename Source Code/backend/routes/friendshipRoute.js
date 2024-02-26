const router = require('express').Router();
const friendshipController = require('../controllers/FriendshipController/FriendshipController');
const { verifyToken } = require('../middleware/verifyToken');


router.route('/sendFriendRequest').post(verifyToken, friendshipController.sendFriendRequest);
router.route('/acceptFriendRequest').put(verifyToken, friendshipController.acceptFriendRequest);
router.route('/rejectFriendRequest').delete(verifyToken, friendshipController.rejectFriendRequest);
router.route('/deleteFriend').delete(verifyToken, friendshipController.deleteFriend);
router.route('/:username/friends').get(verifyToken, friendshipController.getAllFriends);
module.exports = router;