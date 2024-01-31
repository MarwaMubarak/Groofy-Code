import { reqInstance } from "..";
import { authActions } from "../slices/auth-slice";
import { userActions } from "../slices/user-slice";

export interface EditInfo {
  firstname: string;
  lastname: string;
  city: string;
  bio: string;
  country: string;
}

const getUser = (username: string) => {
  return async (dispatch: any) => {
    const loggedUser = JSON.parse(localStorage.getItem("user")!);
    console.log("Token = ", loggedUser.token);
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

const updateUser = (userId: string, editInfo: EditInfo) => {
  return async (dispatch: any) => {
    try {
      const userToken = JSON.parse(localStorage.getItem("user")!).token;
      console.log("Edit Info: ", editInfo);
      const response = await reqInstance.put(`/users/update`, editInfo, {
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

const userThunks = { getUser, updateUser };

export default userThunks;
