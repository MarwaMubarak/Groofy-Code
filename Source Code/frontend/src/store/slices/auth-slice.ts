import { createSlice } from "@reduxjs/toolkit";

const authInitialState = {
  user: null,
  existingGameId: null,
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
      state.existingGameId = (state.user as any)?.existingGameId;
    },
    logout(state) {
      state.user = null;
      state.existingGameId = null;
    },
    setUser(state, action) {
      state.user = { ...(state.user || {}), ...action.payload };
      state.existingGameId = (state.user as any)?.existingGameId;
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
    setUserGameId(state, action) {
      (state.user! as any).existingGameId = action.payload;
      state.existingGameId = action.payload;
    },
    setExistingInvitation(state, action) {
      if (state.user !== null) {
        (state.user as any).existingInvitationId = action.payload;
      }
    },
  },
});

export const authActions = authSlice.actions;
export default authSlice.reducer;
