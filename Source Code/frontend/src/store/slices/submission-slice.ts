import { createSlice } from "@reduxjs/toolkit";

const submissionInitialState = {
  status: "",
  message: "",
  submission: "",
  isLoading: false,
  submitState: "",
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
    setSubmitState(state, action) {
      state.submitState = action.payload;
    },
  },
});

export const submissionActions = submissionSlice.actions;

export default submissionSlice.reducer;
