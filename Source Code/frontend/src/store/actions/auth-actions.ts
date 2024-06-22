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
      localStorage.setItem("token", response.data.body.token);
      const { token, ...body } = response.data.body;
      dispatch(authActions.login(body));
    } catch (error: any) {
      throw error;
    }
  };
};

const logout = () => {
  return (dispatch: any) => {
    dispatch(authActions.logout());
    dispatch(postActions.setStatus(""));
    dispatch(postActions.setMessage(""));
    localStorage.removeItem("token");
  };
};

const signup = (userData: UserProps) => {
  return async (dispatch: any) => {
    try {
      await reqInstance.post("/register", {
        username: userData.username,
        displayName: userData.displayName,
        email: userData.email,
        password: userData.password,
        country: userData.country.name,
      });
    } catch (error: any) {
      throw error;
    }
  };
};

const authThunks = { login, logout, signup };

export default authThunks;
