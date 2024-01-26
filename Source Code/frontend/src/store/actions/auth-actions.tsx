import { reqInstance } from "..";
import { UserProps } from "../../shared/types";
import { authActions } from "../slices/auth-slice";
// import { toast } from "react-toastify";

const login = (userData: UserProps) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.post("/login", {
        email: userData.email,
        password: userData.password,
      });
      console.log(response.data);
      dispatch(authActions.login());
      // toast.success("Login successful");
    } catch (error: any) {
      // toast.error(error.response.data.error);
    }
  };
};

const logout = () => {};

const signup = (userData: UserProps) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.post("/register", {
        username: userData.username,
        email: userData.email,
        password: userData.password,
        firstname: userData.firstname,
        lastname: userData.lastname,
        country: userData.country,
      });
      console.log(response.data);
      // toast.success("Created account succesfully");
    } catch (error: any) {
      // toast.error(error.response.data.error);
    }
  };
};

const authThunks = { login, logout, signup };

export default authThunks;
