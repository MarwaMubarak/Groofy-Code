const asyncHandler = require("express-async-handler");
const { User } = require("../../models/userModel");
const { Friendship } = require("../../models/friendshipModel");
const { unsuccessfulRes, successfulRes } = require("../../utilities/responseFormate");


/**----------------------------------------
 *  @description  send friend request 
 *  @rounter      /sendFriendRequest
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/

module.exports.sendFriendRequest = asyncHandler(async(req, res) => {

    try {
        const { receiverId } = req.body;
        const senderId = req.user.id;
        if (!senderId) {
            return res.status(400).json(unsuccessfulRes("Unauthorized User!"));

        }
        if (senderId == receiverId) {
            return res.status(400).json(unsuccessfulRes("Not allowed to add yourself!"));
        }
        const userFound = await User.findById(receiverId);
        if (!userFound) {
            return res
                .status(404)
                .json({ success: false, message: "User not found!" });
        }
        const found1 = await Friendship.findOne({ senderId: senderId, receiverId: receiverId });
        const found2 = await Friendship.findOne({ senderId: receiverId, receiverId: senderId });

        if (!found1 && !found2) {
            const newFriendship = new Friendship({ senderId: senderId, receiverId: receiverId });
            await newFriendship.save();
            return res
                .status(200)
                .json(successfulRes("Request send Successfully!", newFriendship));
        } else {
            if (found1) {
                if (found1.status == 'accepted') {
                    return res
                        .status(400)
                        .json(unsuccessfulRes("There is already Friendship!"));

                } else if (found1.status == 'pending') {
                    return res
                        .status(400)
                        .json(unsuccessfulRes("There is already Pending Request!"));

                }
            }
            if (found2) {
                if (found2.status == 'accepted') {
                    return res
                        .status(400)
                        .json(unsuccessfulRes("There is already Friendship!"));

                } else if (found2.status == 'pending') {
                    return res
                        .status(400)
                        .json(unsuccessfulRes("There is already Pending Request!"));

                }
            }
        }

    } catch (error) {
        return res
            .status(500)
            .json(unsuccessfulRes("Internal server error"));
    }


});

/**----------------------------------------
 *  @description  accept friend request 
 *  @route        /acceptFriendRequest
 *  @method       PUT
 *  @access       private (users only)
 -----------------------------------------*/
module.exports.acceptFriendRequest = asyncHandler(async(req, res) => {
    try {
        const { requestId } = req.body;
        const userId = req.user.id;
        if (!userId) {
            return res.status(400).json(unsuccessfulRes("Unauthorized User!"));
        }
        const friendship = await Friendship.findOne({ _id: requestId, receiverId: userId });
        console.log(friendship);
        if (!friendship) {
            return res
                .status(404)
                .json(unsuccessfulRes("Friend Request Not Found!"));
        }
        console.log(friendship.status);
        if (friendship.status == 'pending') {
            friendship.status = 'accepted';
            await friendship.save();
            return res.status(200).json(successfulRes("Accepted Successfully!", friendship));
        }
        if (friendship.status == 'accepted') {
            return res.status(400).json(successfulRes("Already Accepted!"));
        }

    } catch (error) {
        return res
            .status(500)
            .json(unsuccessfulRes("Internal server error"));
    }

});

/**----------------------------------------
 *  @description  reject friend request
 *  @route        /rejectFriendRequest 
 *  @method       DELETE
 *  @access       private (users only)
 -----------------------------------------*/

module.exports.rejectFriendRequest = asyncHandler(async(req, res) => {
    try {
        const { requestId } = req.body;
        const userId = req.user.id;
        if (!userId) {
            return res.status(400).json(unsuccessfulRes("Unauthorized User!"));
        }
        const friendship = await Friendship.findOne({ _id: requestId, receiverId: userId });
        if (!friendship) {
            return res
                .status(404)
                .json(unsuccessfulRes("Friend Request Not Found!"));
        }
        if (friendship.status == 'pending') {
            await Friendship.deleteOne({ _id: requestId });
            return res.status(200).json(successfulRes("Rejected Successfully!"));
        }
        if (friendship.status == 'accepted') {
            return res.status(400).json(successfulRes("The Request Accepted Before!"));
        }

    } catch (error) {
        return res
            .status(500)
            .json(unsuccessfulRes("Internal server error"));
    }

});



/**----------------------------------------
 *  @description  delete friend
 *  @route        /deleteFriend 
 *  @method       DELETE
 *  @access       private (users only)
 -----------------------------------------*/

module.exports.deleteFriend = asyncHandler(async(req, res) => {
    try {
        const { requestId } = req.body;
        const userId = req.user.id;
        if (!userId) {
            return res.status(400).json(unsuccessfulRes("Unauthorized User!"));
        }
        const friendship = await Friendship.findOne({ _id: requestId });
        if (!friendship) {
            return res
                .status(404)
                .json(unsuccessfulRes("Friend Request Not Found!"));
        }
        if (friendship.status == 'accepted') {
            await Friendship.deleteOne({ _id: requestId });
            return res.status(200).json(successfulRes("Deleted Successfully!"));
        }
        if (friendship.status == 'pending') {
            return res.status(400).json(successfulRes("Not Accepted Request!"));
        }

    } catch (error) {
        return res
            .status(500)
            .json(unsuccessfulRes("Internal server error"));
    }

});

/**----------------------------------------
 *  @description  get all friends 
 *  @route        /api/allfriends 
 *  @method       GET
 *  @access       private (users only)
 -----------------------------------------*/

module.exports.getAllFriends = asyncHandler(async(req, res) => {

    try {
        // 1. Identify the user making the request
        const userId = req.user.id;
        const sender = await Friendship.find({ senderId: userId, status: 'accepted' });
        const receiver = await Friendship.find({ receiverId: userId, status: 'accepted' });

        const friends = { sender, receiver };
        console.log(friends);
        // 2. Return the list of friends
        return res.status(200).json(successfulRes("Successfully!", friends));
    } catch (error) {
        return res
            .status(500)
            .json(unsuccessfulRes("Internal server error"));
    }

});