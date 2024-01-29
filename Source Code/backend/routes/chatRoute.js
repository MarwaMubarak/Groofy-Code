// routes/chat.js
const router = require('express').Router();
const chatController = require('../controllers/ChatController/ChatController');

// Create a new chat
router.post('/chats', chatController.createChat);

// Get messages in a chat
router.get('/chats/:chatID/messages', chatController.getChatMessages);

// Add a message to a chat
router.post('/chats/:chatID/messages', chatController.addMessageToChat);


module.exports = router;
