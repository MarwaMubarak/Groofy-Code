import { createSlice } from "@reduxjs/toolkit";

const submissionInitialState = {
  status: "",
  message: "",
  submission: "",
  isLoading: false,
};

const submissionSlice = createSlice({
  initialState: submissionInitialState,
  name: "submission",
  reducers: {
    setSubmission(state, action) {
      state.status = action.payload.status;
      state.message = action.payload.message;
      state.submission = action.payload.body;
    },
    setIsLoading(state, action) {
      state.isLoading = action.payload;
    },
  },
});

export const submissionActions = submissionSlice.actions;

export default submissionSlice.reducer;
