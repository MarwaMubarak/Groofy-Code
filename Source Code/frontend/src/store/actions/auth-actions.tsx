import { reqInstance } from "..";
import { UserProps } from "../../shared/types";
import { authActions } from "../slices/auth-slice";

const login = (userData: UserProps) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.post("/login", {
        email: userData.email,
        password: userData.password,
      });
      localStorage.setItem("user", JSON.stringify(response.data.body));
      dispatch(authActions.login(response.data.body));
      // toast.success("Login successful");
    } catch (error: any) {
      dispatch(authActions.setErrorMessage(error.response.data.message));
      // throw error.response.data.message;
    }
  };
};

const logout = () => {
  return (dispatch: any) => {
    dispatch(authActions.logout());
    localStorage.removeItem("user");
  };
};

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
      return error;
      // toast.error(error.response.data.error);
    }
  };
};

const setErrorMessage = (message: string | null) => {
  return (dispatch: any) => {
    dispatch(authActions.setErrorMessage(message));
  };
};

const authThunks = { login, logout, signup, setErrorMessage };

export default authThunks;
