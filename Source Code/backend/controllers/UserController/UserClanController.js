const asyncHandler = require("express-async-handler");
const { User } = require('../../models/userModel');
const { Clan } = require('../../models/clanModel');


UserSchema.methods.joinClan = async function (clanId) {
    try {
      const clan = await Clan.findById(clanId);
  
      if (!clan) {
        throw new Error("Clan not found");
      }
  
      if (this.clan) {
        throw new Error("User is already a member of another clan");
      }
  
      this.clan = clanId;
      clan.members.push(this._id);
  
      await Promise.all([this.save(), clan.save()]);
  
      return { success: true, message: "User joined the clan successfully", clanId };
    } catch (error) {
      return { success: false, message: error.message };
    }
};
  
/**----------------------------------------
 *  @description  Join Clan
 *  @route        /api/user/joinclan
 *  @method       POST
 *  @access       private (users only)
 -----------------------------------------*/
module.exports.joinClan = asyncHandler(async (req, res) => {
const userId = req.user.id;
const { clanId } = req.body;

try {
    const user = await User.findById(userId);

    if (!user) {
    return res.status(404).json({ success: false, message: "User not found" });
    }

    const result = await user.joinClan(clanId);

    if (result.success) {
    res.status(200).json({ success: true, message: result.message, clanId: result.clanId });
    } else {
    res.status(400).json({ success: false, message: result.message });
    }
} catch (error) {
    res.status(500).json({ success: false, message: "Internal Server Error" });
}
});



UserSchema.methods.leaveClan = async function () {
    try {
      if (!this.clan) {
        throw new Error("User is not a member of any clan");
      }
  
      const clan = await Clan.findById(this.clan);
  
      if (!clan) {
        throw new Error("Clan not found");
      }
  
      // Remove user's ID from the clan's members array
      clan.members = clan.members.filter(memberId => memberId.toString() !== this._id.toString());
  
      // Update user's clan field to null
      this.clan = null;
  
      await Promise.all([this.save(), clan.save()]);
  
      return { success: true, message: "User left the clan successfully" };
    } catch (error) {
      return { success: false, message: error.message };
    }
  };
  
  /**----------------------------------------
   *  @description  Leave Clan
   *  @route        /api/user/leaveclan
   *  @method       POST
   *  @access       private (users only)
   -----------------------------------------*/
  module.exports.leaveClan = asyncHandler(async (req, res) => {
    const userId = req.user.id;
  
    try {
      const user = await User.findById(userId);
  
      if (!user) {
        return res.status(404).json({ success: false, message: "User not found" });
      }
  
      const result = await user.leaveClan();
  
      if (result.success) {
        res.status(200).json({ success: true, message: result.message });
      } else {
        res.status(400).json({ success: false, message: result.message });
      }
    } catch (error) {
      res.status(500).json({ success: false, message: "Internal Server Error" });
    }
  });