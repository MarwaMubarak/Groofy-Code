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
        (post: any) => post._id === action.payload._id
      );
      console.log("Post Index", postIdx);
      state.body[postIdx].content = action.payload.content;
      state.body[postIdx].updatedAt = action.payload.updatedAt;
    },
    deletePost(state, action) {
      state.body = state.body.filter(
        (post: any) => post._id !== action.payload
      );
    },

    likePost(state, action) {
      const postIdx = state.body.findIndex(
        (post: any) => post._id === action.payload.postID
      );
      const userLiked = state.body[postIdx].likes.includes(
        action.payload.userID
      );
      if (!userLiked) {
        state.body[postIdx].likes.push(action.payload.userID);
      } else {
        state.body[postIdx].likes = state.body[postIdx].likes.filter(
          (like: any) => like !== action.payload.userID
        );
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
