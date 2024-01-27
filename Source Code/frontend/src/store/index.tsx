import { configureStore } from "@reduxjs/toolkit";
import { authReducer } from "./slices";
import { postReducer } from "./slices";
import axios from "axios";

const store = configureStore({
  reducer: { auth: authReducer, post: postReducer },
});

export const reqInstance = axios.create({ baseURL: "http://localhost:8000" });

export default store;
