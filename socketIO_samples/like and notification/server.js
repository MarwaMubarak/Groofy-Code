const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const cors = require("cors"); // Import the cors middleware

const PORT = process.env.PORT || 3000;
const app = express();
const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
  },
});


io.on("connection", (socket) => {
  console.log(`[NEW CONNECTION] ${socket.id} connected.`);

  // When a client sends a like
  socket.on("push_notification", (data) => {
    const { receiverId, like } = data;

    // Sending notification to the receiving client
    io.to(receiverId).emit("receive_notification", { senderId: socket.id, like });
  });

  // When a client disconnects
  socket.on("disconnect", () => {
    console.log(`[DISCONNECTION] ${socket.id} disconnected.`);
  });
});

server.listen(PORT, () => {
  console.log(`[LISTENING] Server is listening on port ${PORT}`);
});
