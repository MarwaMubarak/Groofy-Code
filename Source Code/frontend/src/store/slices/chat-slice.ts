import { createSlice } from "@reduxjs/toolkit";

const chatInitialState = {
  chats: [] as any[],
  clanMessages: [] as any[],
  userMessages: [] as any[],
  chatUser: null,
};

const chatSlice = createSlice({
  name: "chat",
  initialState: chatInitialState,
  reducers: {
    clearChat(state) {
      state.chats = [];
      state.clanMessages = [];
      state.userMessages = [];
      state.chatUser = null;
    },
    setChatUser(state, action) {
      state.chatUser = action.payload;
    },
    setChats(state, action) {
      state.chats = action.payload;
    },
    setClanMessages(state, action) {
      state.clanMessages = action.payload;
    },
    setUserMessages(state, action) {
      state.userMessages = action.payload;
    },
    addClanMessage(state, action) {
      state.clanMessages.push(action.payload);
    },
    addUserMessage(state, action) {
      state.userMessages.push(action.payload);
    },
    clearClanMessages(state) {
      state.clanMessages = [];
    },
    clearUserMessages(state) {
      state.userMessages = [];
    },
    readUserMessages(state, action) {
      state.chats
        .filter((msg: any) => msg.id === action.payload)
        .map((msg: any) => (msg.unreadCount = 0));
    },
  },
});

export const chatActions = chatSlice.actions;

export default chatSlice.reducer;
