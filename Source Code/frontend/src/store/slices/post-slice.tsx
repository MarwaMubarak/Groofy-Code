import { createSlice } from "@reduxjs/toolkit";

interface Post {
  _id: string;
  content: string;
}

const postInitialState = {
  posts: [] as Post[], // Specify the type of the posts array as Post[]
};

const postSlice = createSlice({
  name: "post",
  initialState: postInitialState,
  reducers: {
    setPost(state, action) {
      state.posts = action.payload;
    },
    addPost(state, action) {
      state.posts.push(action.payload as Post);
    },
    deletePost(state, action) {
      state.posts = state.posts.filter(
        (post: Post) => post._id !== action.payload
      );
    },
    updatePost(state, action) {
      const postIndex = state.posts.findIndex(
        (post: Post) => post._id === action.payload._id
      );
      state.posts[postIndex] = action.payload;
    },
  },
});

export const postActions = postSlice.actions;
export default postSlice.reducer;
