const mongoose = require("mongoose");


const FriendshipSchema = mongoose.Schema({
    friend1Id: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",

    },
    friend2Id: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",

    },

});
const Friendship = mongoose.model("Friendship", FriendshipSchema);

module.exports = {
    Friendship,
}