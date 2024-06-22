import { configureStore } from "@reduxjs/toolkit";
import {
  authReducer,
  postReducer,
  userReducer,
  matchReducer,
  submissionReducer,
  socketReducer,
  clanReducer,
  friendReducer,
  notifyReducer,
} from "./slices";
import axios from "axios";

const store = configureStore({
  reducer: {
    auth: authReducer,
    post: postReducer,
    user: userReducer,
    match: matchReducer,
    submission: submissionReducer,
    socket: socketReducer,
    clan: clanReducer,
    friend: friendReducer,
    notify: notifyReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export const reqInstance = axios.create({ baseURL: "http://localhost:8080" });

export default store;
