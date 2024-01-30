const asyncHandler = require("express-async-handler");
const { User } = require("../../models/userModel");
const { Friendship } = require("../../models/friendshipModel");
const { unsuccessfulRes, successfulRes } = require("../../utilities/responseFormate");


/**----------------------------------------
 *  @description  add friend 
 *  @rounter      /api/addfriend 
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/

module.exports.addFriend = asyncHandler(async(req, res) => {

    try {
        const { friendId } = req.body;
        const friend1Id = req.user.id;
        if (friend1Id == friendId) {
            return res.status(400).json(unsuccessfulRes("Not allowed to add yourself!"));
        }
        const friend2 = await User.findById(friendId);


        if (!friend2) {
            return res
                .status(404)
                .json({ success: false, message: "Friend not found" });
        }
        const found1 = await Friendship.findOne({ friend1Id: friend1Id, friend2Id: friendId });
        const found2 = await Friendship.findOne({ friend1Id: friendId, friend2Id: friend1Id });

        console.log(1);
        if (!found1 && !found2) {
            const newFriendship = new Friendship({ friend1Id: friend1Id, friend2Id: friendId });
            await newFriendship.save();
            return res
                .status(200)
                .json(successfulRes("Added Successfully!", newFriendship));
        } else {
            return res
                .status(400)
                .json(unsuccessfulRes("There are already Friendship!"));
        }

    } catch (error) {
        return res
            .status(500)
            .json(unsuccessfulRes("Internal server error"));
    }


});

/**----------------------------------------
 *  @description  remove friend 
 *  @route        /api/removefriend 
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/

module.exports.removeFriend = asyncHandler(async(req, res) => {
    try {
        const { friendId } = req.body;
        const friend1Id = req.user.id;
        const friend2 = await User.findById(friendId);
        if (!friend2) {
            return res
                .status(404)
                .json({ success: false, message: "Friend not found" });
        }
        const found1 = await Friendship.findOne({ friend1Id: friend1Id, friend2Id: friendId });
        const found2 = await Friendship.findOne({ friend1Id: friendId, friend2Id: friend1Id });

        if (found1 || found2) {
            console.log(1);
            console.log(found1);
            console.log(found2);

            if (found1) {
                await Friendship.deleteOne({ friend1Id: friend1Id, friend2Id: friendId });
            } else {
                await Friendship.deleteOne({ friend1Id: friendId, friend2Id: friend1Id });
            }

            return res
                .status(200)
                .json(successfulRes("Deleted Successfully!"));
        } else {
            return res
                .status(400)
                .json(unsuccessfulRes("There are already not Friendship!"));
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
        console.log(1);
        const friends1 = await Friendship.find({ friend1Id: userId });
        const friends2 = await Friendship.find({ friend2Id: userId });

        const friends = { friends1, friends2 };
        console.log(friends);
        // 2. Return the list of friends
        return res.status(200).json(successfulRes("Successfully!", friends));
    } catch (error) {
        return res
            .status(500)
            .json(unsuccessfulRes("Internal server error"));
    }

});