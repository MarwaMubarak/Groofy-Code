const express = require("express");
const http = require("http");
const socketIo = require("socket.io");

const PORT = process.env.PORT || 3000;
const app = express();
const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
  },
});

// Dictionary to store room information
const userRoom = {};
const rooms = [];

io.on("connection", (socket) => {
  console.log(`[NEW CONNECTION] Socket ${socket.id} connected.`);

  // Function to handle creating a new room
  socket.on("create_room", (roomName) => {
    console.log(`[CREATE ROOM] Room '${roomName}' created by ${socket.id}.`);
    userRoom[socket.id] = roomName;
    rooms.push(roomName);
    socket.join(roomName);
    socket.emit("room_created", roomName);
  });

  // Function to handle joining an existing room
  socket.on("join_room", (roomName) => {
    console.log(`[JOIN ROOM] ${socket.id} joined room '${roomName}'.`);
    if (roomName in rooms) {
      socket.emit("room_not_found", roomName);
      return;
    }
    userRoom[socket.id] = roomName;
    socket.join(roomName);
    socket.emit("room_joined", roomName);
  });

  // Function to handle sending a message to all members of a room
  socket.on("send_message", (data) => {
    const { message } = data;
    console.log(
      `[SEND MESSAGE] ${socket.id} sent message to room '${userRoom[socket.id]}': ${message}`
    );
    io.to(userRoom[socket.id]).emit("message_received", { sender: socket.id, message });
  });

  // Function to handle disconnecting from the server
  socket.on("disconnect", () => {
    console.log(`[DISCONNECTION] Socket ${socket.id} disconnected.`);
    console.log(`[ROOM LEAVE] ${socket.id} left room '${userRoom[socket.id]}'.`);
    delete userRoom[socket.id];
  });
});

server.listen(PORT, () => {
  console.log(`[LISTENING] Server is listening on port ${PORT}`);
});
