import { createSlice } from "@reduxjs/toolkit";

const popupInitialState = {
  show: false,
  body: {},
};

const popupSlice = createSlice({
  name: "popup",
  initialState: popupInitialState,
  reducers: {
    setContent(state, action) {
      state.show = action.payload.show;
      state.body = action.payload.body;
    },
    setShow(state, action) {
      state.show = action.payload;
    },
    setBody(state, action) {
      state.body = action.payload;
    },
  },
});

export const popupActions = popupSlice.actions;

export default popupSlice.reducer;
