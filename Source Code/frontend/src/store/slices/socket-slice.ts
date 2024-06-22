import { createSlice } from "@reduxjs/toolkit";

const socketInitialState = {
  stompClient: null,
};

const socketSlice = createSlice({
  initialState: socketInitialState,
  name: "socket",
  reducers: {
    setStompClient(state, action) {
      state.stompClient = action.payload;
    },
  },
});

export const socketActions = socketSlice.actions;

export default socketSlice.reducer;
