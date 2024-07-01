import { createSlice } from "@reduxjs/toolkit";

const chatInitialState = {
  messages: [] as any[],
};

const chatSlice = createSlice({
  name: "chat",
  initialState: chatInitialState,
  reducers: {
    setMessages(state, action) {
      state.messages = action.payload;
    },
    addMessage(state, action) {
      state.messages.push(action.payload);
    },
    clearMessages(state) {
      state.messages = [];
    },
  },
});

export const chatActions = chatSlice.actions;

export default chatSlice.reducer;
