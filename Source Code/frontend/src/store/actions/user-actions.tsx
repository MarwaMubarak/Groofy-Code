import { reqInstance } from "..";
import { userActions } from "../slices/user-slice";

interface EditInfo {
  firstname: string;
  lastname: string;
  country: string;
  bio: string;
}

const getUser = (username: string) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.get(`/user/${username}`);
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
      const response = await reqInstance.put(`/user/${userId}`, editInfo, {
        headers: {
          Authorization: `Bearer ${userToken}`,
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

const userThunks = { getUser, updateUser };

export default userThunks;
