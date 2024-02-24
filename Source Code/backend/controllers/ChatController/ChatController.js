const Chat = require('../../models/chatModel');
const asyncHandler = require('express-async-handler');

// Create a new chat
exports.createChat = asyncHandler(async(req, res) => {
    const { clanID } = req.body;
    const clanId = mongoose.Types.ObjectId(clanID);
    const chat = new Chat({ clanId });
    await chat.save();
    res.status(201).json({ message: "You have create a chat sucessfully. " });
});

// Get messages in a chat
exports.getChatMessages = async(req, res) => {
    try {
        const { chatID } = req.params;
        const chat = await Chat.findById(chatID).populate('messages');
        res.status(200).json(chat.messages);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
};

// Add a message to a chat
exports.addMessageToChat = async(req, res) => {
    try {
        const { chatID } = req.params;
        const { content, sender } = req.body;

        // Assuming you have a Message model
        const Message = require('../models/Message');
        const message = new Message({ content, sender });
        await message.save();

        const chat = await Chat.findByIdAndUpdate(
            chatID, { $push: { messages: message._id } }, { new: true }
        );

        res.status(201).json(message);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
};

// Other controller functions for updating and deleting messages can be added here