const mongoose = require("mongoose");

const ChatSchema = new mongoose.Schema(
  {
    clanID: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Clan",
    },
    messages: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "Message",
      },
    ],
  },
  { timestamps: true }
);

const Chat = mongoose.model("Chat", ChatSchema);

module.exports = Chat;
