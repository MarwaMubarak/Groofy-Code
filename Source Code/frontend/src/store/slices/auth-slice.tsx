import { createSlice } from "@reduxjs/toolkit";

const authInitialState = {
  user: null,
  errorMessage: "",
};

const authSlice = createSlice({
  name: "auth",
  initialState: authInitialState,
  reducers: {
    login(state, action) {
      state.user = action.payload;
    },
    logout(state) {
      state.user = null;
    },
    setUser(state, action) {
      state.user = { ...(state.user || {}), ...action.payload };
    },
    setErrorMessage(state, action) {
      state.errorMessage = action.payload;
    },
  },
});

export const authActions = authSlice.actions;
export default authSlice.reducer;
