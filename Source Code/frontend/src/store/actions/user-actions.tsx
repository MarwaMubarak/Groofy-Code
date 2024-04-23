import { reqInstance } from "..";
import { authActions } from "../slices/auth-slice";
import { userActions } from "../slices/user-slice";

export interface EditInfo {
  displayName: string;
  bio: string;
  country: string;
}

const getUser = (username: string) => {
  return async (dispatch: any) => {
    const loggedUser = JSON.parse(localStorage.getItem("user")!);
    try {
      const response = await reqInstance.get(`/users/${username}`, {
        headers: {
          Authorization: `Bearer ${loggedUser.token}`,
        },
      });
      dispatch(userActions.setUser(response.data.body));
      dispatch(
        userActions.setRes({
          status: response.data.status,
          message: response.data.message,
        })
      );
    } catch (error: any) {
      dispatch(
        userActions.setRes({
          status: error.response.data.status,
          message: error.response.data.message,
        })
      );
    }
  };
};

const updateUser = (editInfo: EditInfo) => {
  return async (dispatch: any) => {
    try {
      const userToken = JSON.parse(localStorage.getItem("user")!).token;
      const response = await reqInstance.put(`/users`, editInfo, {
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
      });
      const newInfo = { ...response.data.body, token: userToken };
      localStorage.setItem("user", JSON.stringify(newInfo));
      dispatch(authActions.setUser(newInfo));
      return response;
    } catch (error: any) {
      return error;
    }
  };
};

const changePassword = (
  currentPassword: string,
  password: string,
  confirmPassword: string
) => {
  return async (dispatch: any) => {
    try {
      const userToken = JSON.parse(localStorage.getItem("user")!).token;
      const response = await reqInstance.put(
        `/users/password`,
        {
          currentPassword: currentPassword,
          password: password,
          confirmPassword: confirmPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${userToken}`,
          },
        }
      );
      return response;
    } catch (error: any) {
      return error;
    }
  };
};

const searchForUsers = (searchQuery: string) => {
  return async (dispatch: any) => {
    try {
      const userToken = JSON.parse(localStorage.getItem("user")!).token;
      const response = await reqInstance.get(`/users/search/${searchQuery}`, {
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
      });
      return response;
    } catch (error: any) {
      return error;
    }
  };
};

const userThunks = { getUser, updateUser, changePassword, searchForUsers };

export default userThunks;
