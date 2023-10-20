const mongoose = require("mongoose");

module.exports = async () => {
  try {
    await mongoose.connect(process.env.MONGODB);
    console.log("Connected To MongoDB");
  } catch (error) {
    console.log("Connection failed", error);
  }
};
