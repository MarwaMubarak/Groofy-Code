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
      state.body = [action.payload, ...state.body];
    },
    updatePost(state, action) {
      const postIdx = state.body.findIndex(
        (post: any) => post.id === action.payload.id
      );
      state.body[postIdx].content = action.payload.content;
      state.body[postIdx].updatedAt = action.payload.updatedAt;
    },
    deletePost(state, action) {
      state.body = state.body.filter((post: any) => post.id !== action.payload);
    },

    likePost(state, action) {
      const postIdx = state.body.findIndex(
        (post: any) => post.id === action.payload.postID
      );
      const userLiked = state.body[postIdx].isLiked === 1;
      if (!userLiked) {
        state.body[postIdx].isLiked = 1;
        state.body[postIdx].likesCnt += 1;
      } else {
        state.body[postIdx].isLiked = 0;
        state.body[postIdx].likesCnt -= 1;
      }
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
