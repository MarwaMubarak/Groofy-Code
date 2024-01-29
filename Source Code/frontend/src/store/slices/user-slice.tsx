import { createSlice } from "@reduxjs/toolkit";

const userInitialState = {
  user: null,
  status: "",
  message: "",
};

const userSlice = createSlice({
  name: "user",
  initialState: userInitialState,
  reducers: {
    setUser(state, action) {
      state.user = action.payload;
    },
    setRes(state, action) {
      state.status = action.payload.status;
      state.message = action.payload.message;
    },
  },
});

export const userActions = userSlice.actions;

export default userSlice.reducer;
