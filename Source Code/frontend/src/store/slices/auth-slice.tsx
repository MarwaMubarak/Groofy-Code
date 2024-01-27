import { createSlice } from "@reduxjs/toolkit";

const authInitialState = {
  token: localStorage.getItem("token") ? localStorage.getItem("token") : null,
  _id: localStorage.getItem("id") ? localStorage.getItem("id") : null,
  user: null,
};

const authSlice = createSlice({
  name: "auth",
  initialState: authInitialState,
  reducers: {
    login(state, action) {
      state = {
        token: action.payload.token,
        _id: action.payload._id,
        user: action.payload,
      };
    },
    logout(state) {
      state.token = null;
      localStorage.removeItem("token");
      localStorage.removeItem("_id");
    },
    setUser(state, action) {
      state.user = action.payload;
    },
  },
});

export const authActions = authSlice.actions;
export default authSlice.reducer;
