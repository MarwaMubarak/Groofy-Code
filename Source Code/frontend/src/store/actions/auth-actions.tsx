import { reqInstance } from "..";
import { UserProps } from "../../shared/types";
import { authActions } from "../slices/auth-slice";
import { postActions } from "../slices/post-slice";

interface LoginProps {
  usernameOrEmail: string;
  password: string;
}

const login = (loginInfo: LoginProps) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.post("/login", {
        emailOrUsername: loginInfo.usernameOrEmail,
        password: loginInfo.password,
      });
      dispatch(authActions.setErrorMessage(""));
      console.log("Response", response);
      localStorage.setItem("user", JSON.stringify(response.data.body));
      dispatch(authActions.login(response.data.body));
    } catch (error: any) {
      console.log("error", error);

      throw error;
    }
  };
};

const logout = () => {
  return (dispatch: any) => {
    dispatch(authActions.logout());
    dispatch(postActions.setStatus(""));
    dispatch(postActions.setMessage(""));
    localStorage.removeItem("user");
  };
};

const signup = (userData: UserProps) => {
  return async (dispatch: any) => {
    try {
      await reqInstance.post("/register", {
        username: userData.username,
        email: userData.email,
        password: userData.password,
        firstname: userData.firstname,
        lastname: userData.lastname,
        country: userData.country,
      });
    } catch (error: any) {
      throw error;
    }
  };
};

const authThunks = { login, logout, signup };

export default authThunks;
