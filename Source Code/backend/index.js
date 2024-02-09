const express = require("express");
const connectToDB = require("./config/connectToDB");
const helmet = require("helmet");
const hpp = require("hpp");
const http = require("http");
const cors = require("cors");
const socketio = require("socket.io");
require("dotenv").config();

// Connect to MongoDB
connectToDB();

// Initializing express.js
const index = express();
server = http.createServer(index);
index.use(express.json());

// Allowing cors policy
index.use(
  cors({
    origin: "*",
    allowedHeaders: ["Content-Type", "authorization"],
  })
);

// Routes
index.use("/", require("./routes/userRoute"));
index.use("/", require("./routes/clanRoute"));
index.use("/", require("./routes/blogRoute"));
index.use("/", require("./routes/postRoute"));
index.use("/", require("./routes/badgeRoute"));
index.use("/", require("./routes/friendshipRoute"));

const io = socketio(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
  },
});

io.on("connection", (socket) => {
  console.log(`[NEW CONNECTION] ${socket.id} connected.`);

  // When a client sends a like
  socket.on("sendFriendRequest", (data) => {
    const { receiverId } = data;
    console.log("hi");
    // Sending notification to the receiving client
    io.to(receiverId).emit("receivedfriendRequest", { senderId: socket.id });
  });
  // socket.on("acceptFriendRequest", (data) => {
  //     const { receiverId, body } = data;

  //     // Sending notification to the receiving client
  //     io.to(receiverId).emit("recievedAcceptedRequest", { senderId: socket.id, body });
  // });

  // When a client disconnects
  socket.on("disconnect", () => {
    console.log(`[DISCONNECTION] ${socket.id} disconnected.`);
  });
});

// Security Measures
index.use(helmet());
index.use(hpp());

// Starting up the server
const Port = process.env.PORT;
index.listen(Port, () => {
  console.log("Server is running successfully");
});
