const mongoose = require("mongoose");

const SubmissionSchema = new mongoose.Schema(
  {
    language: {
      type: String,
      required: true,
    },
    code: {
      type: String,
      required: true,
    },
    users: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
      },
    ],
    problem: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Problem",
    },
    verdict: {
      type: String,
      required: true,
    },
    timelimit: {
      type: String,
      required: true,
    },
    memorylimit: {
      type: String,
      required: true,
    },
  },
  { timestamps: true, toJSON: { virtuals: true }, toObject: { virtuals: true } }
);

// Submission Model
const Submission = mongoose.model("Submission", SubmissionSchema);

module.exports = Submission;
