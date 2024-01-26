import { configureStore } from "@reduxjs/toolkit";
import { authReducer } from "./slices";
import axios from "axios";

const store = configureStore({
  reducer: { auth: authReducer },
});

export const reqInstance = axios.create({ baseURL: "http://localhost:8000" });

export default store;
