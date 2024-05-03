import { configureStore } from "@reduxjs/toolkit";
import {
  authReducer,
  postReducer,
  userReducer,
  matchReducer,
  submissionReducer,
} from "./slices";
import axios from "axios";

const store = configureStore({
  reducer: {
    auth: authReducer,
    post: postReducer,
    user: userReducer,
    match: matchReducer,
    submission: submissionReducer,
  },
});

export const reqInstance = axios.create({ baseURL: "http://localhost:8080" });

export default store;
