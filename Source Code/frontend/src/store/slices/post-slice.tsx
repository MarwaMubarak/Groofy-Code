import { createSlice } from "@reduxjs/toolkit";

const postInitialState = {
  status: "",
  message: "",
  body: [] as any,
};

const postSlice = createSlice({
  name: "post",
  initialState: postInitialState,
  reducers: {
    setPosts(state, action) {
      state.body = action.payload;
    },
    addPost(state, action) {
      state.body.push(action.payload);
    },
    updatePost(state, action) {
      const postIdx = state.body.findIndex(
        (post: any) => post._id === action.payload._id
      );
      state.body[postIdx].content = action.payload.content;
      state.body[postIdx].updatedAt = action.payload.updatedAt;
    },
    deletePost(state, action) {
      state.body = state.body.filter(
        (post: any) => post._id !== action.payload
      );
    },
    setStatus(state, action) {
      state.status = action.payload;
    },
    setMessage(state, action) {
      state.message = action.payload;
    },
  },
});

export const postActions = postSlice.actions;
export default postSlice.reducer;
