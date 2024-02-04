const mongoose = require("mongoose");


const FriendshipSchema = mongoose.Schema({
    senderId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
        required: true,

    },
    receiverId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
        required: true,
    },
    status: {
        type: String,
        enum: ['pending', 'accepted'],
        default: 'pending',
    },
    createdAt: {
        type: Date,
        default: Date.now,
    },

});
const Friendship = mongoose.model("Friendship", FriendshipSchema);

module.exports = {
    Friendship,
}