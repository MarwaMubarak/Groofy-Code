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
      dispatch(authActions.setErrorMessage(""));
      const dispatchResponse = await dispatch(
        authActions.login(response.data.body)
      );
      return dispatchResponse;
    } catch (error: any) {
      return error;
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
    } catch (error: any) {
      return error;
    }
  };
};

const authThunks = { login, logout, signup };

export default authThunks;
