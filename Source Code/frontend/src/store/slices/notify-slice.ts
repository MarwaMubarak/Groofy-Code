import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  body: "",
  status: "",
  message: "",
  notifications: [],
  notifyCnt: "0",
};

const notifySlice = createSlice({
  name: "notify",
  initialState,
  reducers: {
    setBody(state, action) {
      state.body = action.payload;
    },
    setNotifications(state, action) {
      state.notifications = action.payload;
    },
    setNotifyCnt(state, action) {
      state.notifyCnt = action.payload;
    },
    setResponse(state, action) {
      state.status = action.payload.status;
      state.message = action.payload.message;
    },
  },
});

export const notifyActions = notifySlice.actions;

export default notifySlice.reducer;
