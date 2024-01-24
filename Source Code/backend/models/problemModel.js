const mongoose = require("mongoose");

const ProblemSchema = new mongoose.Schema({
  title: {
    type: String,
    required: true,
    minlength: 3,
    maxlength: 120,
  },
  statement: {
    type: String,
    required: true,
    minlength: 10,
    maxlength: 10000,
  },
  input: {
    type: String,
    required: true,
    minlength: 1,
  },
  output: {
    type: String,
    required: true,
    minlength: 1,
  },
  testcases: [
    {
      input: {
        type: String,
        required: true,
        minlength: 1,
      },
      output: {
        type: String,
        required: true,
        minlength: 1,
      },
    },
  ],
  note: {
    type: String,
    minlength: 10,
  },
  timelimit: {
    type: String,
    required: true,
  },
  memorylimit: {
    type: String,
    required: true,
  },
  tags: [
    {
      type: String,
      minlength: 2,
    },
  ],
});

// Problem Model
const Problem = mongoose.model("Problem", ProblemSchema);

module.exports = Problem;
