import { createSlice } from "@reduxjs/toolkit";

const authInitialState = {
  user: null,
  status: null,
  errorMessage: "",
  isUploading: false,
  isDeleting: false,
};

const authSlice = createSlice({
  name: "auth",
  initialState: authInitialState,
  reducers: {
    login(state, action) {
      state.user = action.payload;
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
    updateUserPhoto(state, action) {
      (state.user! as any).photoUrl = action.payload;
    },
    setIsUploading(state, action) {
      state.isUploading = action.payload;
    },
    setIsDeleting(state, action) {
      state.isDeleting = action.payload;
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
