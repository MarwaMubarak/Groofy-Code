import { createSlice } from "@reduxjs/toolkit";

const userInitialState = {
  user: null,
  res: { status: "", message: "" },
};

const userSlice = createSlice({
  name: "user",
  initialState: userInitialState,
  reducers: {
    setUser(state, action) {
      state.user = action.payload;
    },
    setRes(state, action) {
      state.res = action.payload;
    },
    setFriendshipStatus(state, action) {
      (state.user! as any).friendshipStatus = action.payload;
    },
  },
});

export const userActions = userSlice.actions;

export default userSlice.reducer;
