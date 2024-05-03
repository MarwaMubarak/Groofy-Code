import { createSlice } from "@reduxjs/toolkit";

const authInitialState = {
  user: null,
  status: null,
  errorMessage: "",
};

const authSlice = createSlice({
  name: "auth",
  initialState: authInitialState,
  reducers: {
    login(state, action) {
      state.user = action.payload;
      console.log("Slice Status: ", (state.user as any)?.status);
      state.status = (state.user as any)?.status;
    },
    logout(state) {
      state.user = null;
      state.status = null;
    },
    setUser(state, action) {
      state.user = { ...(state.user || {}), ...action.payload };
      state.status = (state.user as any)?.status;
    },
    setErrorMessage(state, action) {
      state.errorMessage = action.payload;
    },
    setStatus(state, action) {
      state.status = action.payload;
    },
  },
});

export const authActions = authSlice.actions;
export default authSlice.reducer;
