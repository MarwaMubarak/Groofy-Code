import { createSlice } from "@reduxjs/toolkit";

const friendSliceInitialState = {
  friends: [],
  friendRequests: [],
  friendsCnt: null,
  requestsCnt: null,
};

const friendSlice = createSlice({
  initialState: friendSliceInitialState,
  name: "friend",
  reducers: {
    setFriends(state, action) {
      state.friends = action.payload;
    },
    setFriendRequests(state, action) {
      state.friendRequests = action.payload;
    },
    setFriendsCnt(state, action) {
      state.friendsCnt = action.payload;
    },
    setRequestsCnt(state, action) {
      state.requestsCnt = action.payload;
    },
    removeFriend(state, action) {
      state.friends = state.friends.filter(
        (friend: any) => friend.friendId !== action.payload
      );
    },
  },
});

export const friendActions = friendSlice.actions;

export default friendSlice.reducer;
