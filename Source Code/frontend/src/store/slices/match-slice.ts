import { createSlice } from "@reduxjs/toolkit";

const matchInitialState = {
  status: null,
  message: null,
  match: null,
  isLoading: false,
  userMatches: [],
};

const matchSlice = createSlice({
  initialState: matchInitialState,
  name: "match",
  reducers: {
    setMatch(state, action) {
      state.status = action.payload.status;
      state.message = action.payload.message;
      state.match = action.payload.body;
    },
    setIsLoading(state, action) {
      state.isLoading = action.payload;
    },
    setUserMatches(state, action) {
      state.status = action.payload.status;
      state.message = action.payload.message;
      state.userMatches = action.payload.body;
    },
  },
});

export const matchActions = matchSlice.actions;
export default matchSlice.reducer;
