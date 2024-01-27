import { createSlice } from "@reduxjs/toolkit";

interface Post {
  _id: string;
  content: string;
  createdAt: string;
  updatedAt: string;
}

const postInitialState = {
  posts: [] as Post[],
};

const postSlice = createSlice({
  name: "post",
  initialState: postInitialState,
  reducers: {
    setPosts(state, action) {
      state.posts = action.payload;
    },
    addPost(state, action) {
      state.posts.push(action.payload);
    },
    updatePost(state, action) {
      const postIdx = state.posts.findIndex(
        (post: Post) => post._id === action.payload._id
      );
      state.posts[postIdx] = action.payload;
    },
    deletePost(state, action) {
      state.posts = state.posts.filter(
        (post: Post) => post._id !== action.payload
      );
    },
  },
});

export const postActions = postSlice.actions;
export default postSlice.reducer;
