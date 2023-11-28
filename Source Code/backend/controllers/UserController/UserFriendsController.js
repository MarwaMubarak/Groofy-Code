const asyncHandler = require("express-async-handler");
const { User } = require('../../models/userModel');

/**----------------------------------------
 *  @description  add friend 
 *  @rounter      /api/addfriend 
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/

 module.exports.addFriend = asyncHandler(async (req, res) => {
    // 1. Identify the user making the request
    const userId = req.user.id; // Assuming you are using middleware to get the user from the token
  
    // 2. Find the user to be added as a friend
    const friendId = req.body.friendId; // Assuming friendId is sent in the request body
    const friend = await User.findById(friendId);
  
    if (!friend) {
      return res.status(404).json({ success: false, message: "Friend not found" });
    }
  
    // 3. Update the user's "friends" array to include the new friend
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ success: false, message: "User not found" });
    }
  
    if (!user.friends.includes(friendId)) {
      user.friends.push(friendId);
    } else {
      return res.status(400).json({ success: false, message: "Friend already added" });
    }
  
    // 4. Save the changes to the database
    await user.save();
  
    // 5. Return a response indicating the success or failure of the operation
    res.status(200).json({ success: true, message: "Friend added successfully", friend });
  });


  /**----------------------------------------
 *  @description  remove friend 
 *  @route        /api/auth/removefriend 
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/

module.exports.removeFriend = asyncHandler(async (req, res) => {
    // 1. Identify the user making the request
    const userId = req.user.id;
  
    // 2. Find the user to be removed as a friend
    const friendId = req.body.friendId; // Assuming friendId is sent in the request body
    const friend = await User.findById(friendId);
  
    if (!friend) {
      return res.status(404).json({ success: false, message: "Friend not found" });
    }
  
    // 3. Update the user's "friends" array to remove the friend
    const user = await User.findById(userId);
    if (!user) {
      return res.status(404).json({ success: false, message: "User not found" });
    }
  
    const friendIndex = user.friends.indexOf(friendId);
    if (friendIndex !== -1) {
      user.friends.splice(friendIndex, 1);
    } else {
      return res.status(400).json({ success: false, message: "Friend not found in user's friend list" });
    }
  
    // 4. Save the changes to the database
    await user.save();
  
    // 5. Return a response indicating the success or failure of the operation
    res.status(200).json({ success: true, message: "Friend removed successfully", friend });
});

/**----------------------------------------
 *  @description  get all friends 
 *  @route        /api/auth/allfriends 
 *  @method       GET
 *  @access       private (users only)
 -----------------------------------------*/

 module.exports.getAllFriends = asyncHandler(async (req, res) => {
    // 1. Identify the user making the request
    const userId = req.user.id;
  
    // 2. Find the user and populate the 'friends' field to get friend details
    const user = await User.findById(userId).populate("friends", "username email");
  
    if (!user) {
      return res.status(404).json({ success: false, message: "User not found" });
    }
  
    // 3. Return the list of friends
    res.status(200).json({ success: true, friends: user.friends });
  });

