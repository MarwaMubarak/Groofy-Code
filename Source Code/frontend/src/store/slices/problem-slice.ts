import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  problem: null,
  problemURL: null,
  gameID: null,
  isLoading: false,
  error: null,
};

const problemSlice = createSlice({
  name: "problem",
  initialState,
  reducers: {
    setProblem(state, action) {
      state.problem = action.payload;
    },
    setLoading(state, action) {
      state.isLoading = action.payload;
    },
    setError(state, action) {
      state.error = action.payload;
    },
    setProblemURL(state, action) {
      state.problemURL = action.payload;
    },
    setGameID(state, action) {
      state.gameID = action.payload;
    },
  },
});

export const problemActions = problemSlice.actions;

export default problemSlice.reducer;
