const mongoose = require("mongoose");

const MatchSchema = new mongoose.Schema(
  {
    team1: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],
    team2: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],
    team1Points: {
      type: Number,
      default: 0,
      min: -1000,
    },
    team2Points: {
      type: Number,
      default: 0,
      min: -1000,
    },
    winner: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
    },
    duration: {
      type: Number,
      required: true,
    },
  },
  { timestamps: true, toJSON: { virtuals: true }, toObject: { virtuals: true } }
);

// Match Model
const Match = mongoose.model("Match", MatchSchema);
module.exports = Match;
