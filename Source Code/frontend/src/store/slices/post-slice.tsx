import { createSlice } from "@reduxjs/toolkit";

interface Post {
  _id: string;
  content: string;
}

const postInitialState = {
  posts: [] as Post[],
};

const postSlice = createSlice({
  name: "post",
  initialState: postInitialState,
  reducers: {
    setPost(state, action) {
      return {
        ...state,
        posts: action.payload,
      };
    },
    addPost(state, action) {
      return {
        ...state,
        posts: [...state.posts, action.payload],
      };
    },
    deletePost(state, action) {
      return {
        ...state,
        posts: state.posts.filter((post: Post) => post._id !== action.payload),
      };
    },
    updatePost(state, action) {
      return {
        ...state,
        posts: state.posts.map((post: Post) => {
          if (post._id === action.payload._id) {
            return action.payload;
          }
          return post;
        }),
      };
    },
  },
});

export const postActions = postSlice.actions;
export default postSlice.reducer;
