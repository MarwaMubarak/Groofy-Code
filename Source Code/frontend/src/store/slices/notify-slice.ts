import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  body: "",
  status: "",
  message: "",
  notifications: [],
  notifyCnt: 0,
  messageNotifyCnt: 0,
  friendNotifyCnt: 0,
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
    setNotificationsCnt(state, action) {
      state.notifyCnt = action.payload.notifyCnt;
      state.messageNotifyCnt = action.payload.messageNotifyCnt;
      state.friendNotifyCnt = action.payload.friendNotifyCnt;
    },
    setNotifyCnt(state, action) {
      state.notifyCnt = action.payload;
    },
    setMessageNotifyCnt(state, action) {
      state.messageNotifyCnt = action.payload;
    },
    setFriendNotifyCnt(state, action) {
      state.friendNotifyCnt = action.payload;
    },
    setResponse(state, action) {
      state.status = action.payload.status;
      state.message = action.payload.message;
    },
    removeNotification(state, action) {
      state.notifications = state.notifications.filter(
        (notify: any) => notify.id !== action.payload
      );
    },
  },
});

export const notifyActions = notifySlice.actions;

export default notifySlice.reducer;
