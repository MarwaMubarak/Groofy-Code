import { createSlice } from "@reduxjs/toolkit";

const toastInitialState = {
  toastShow: false,
};

const toastSlice = createSlice({
  name: "toast",
  initialState: toastInitialState,
  reducers: {
    setToastShow: (state, action) => {
      state.toastShow = action.payload;
    },
  },
});

export const toastActions = toastSlice.actions;

export default toastSlice.reducer;
