import { configureStore } from "@reduxjs/toolkit";
import { authReducer, postReducer, userReducer } from "./slices";
import axios from "axios";

const store = configureStore({
  reducer: { auth: authReducer, post: postReducer, user: userReducer },
});

export const reqInstance = axios.create({ baseURL: "http://localhost:8000" });

export default store;
