const socketIO = require("socket.io");

module.exports = (server) => {
  const io = socketIO(server);

  io.on("connection", (socket) => {
    console.log(`[NEW CONNECTION] ${socket.id} connected.`);

    // When a client sends a like
    socket.on("push_notification", (data) => {
      const { receiverId, like } = data;

      // Sending notification to the receiving client
      io.to(receiverId).emit("receive_notification", {
        senderId: socket.id,
        like,
      });
    });

    // When a client disconnects
    socket.on("disconnect", () => {
      console.log(`[DISCONNECTION] ${socket.id} disconnected.`);
    });
  });
};
