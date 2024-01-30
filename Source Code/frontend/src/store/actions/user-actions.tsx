import { reqInstance } from "..";
import { userActions } from "../slices/user-slice";

export interface EditInfo {
  firstName: string;
  lastName: string;
  city: string;
  bio: string;
  country: { name: string; code: string };
}

const getUser = (username: string) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.get(`/user/${username}`);
      const dispatchResponse = await dispatch(
        userActions.setUser(response.data.body)
      );
      dispatch(
        userActions.setRes({
          status: response.data.status,
          message: response.data.message,
        })
      );
      return dispatchResponse;
    } catch (error: any) {
      dispatch(
        userActions.setRes({
          status: error.response.data.status,
          message: error.response.data.message,
        })
      );
      return error;
    }
  };
};

const updateUser = (userId: string, editInfo: EditInfo) => {
  return async (dispatch: any) => {
    try {
      console.log("MY INFO", editInfo);
      const userToken = JSON.parse(localStorage.getItem("user")!).token;
      const response = await reqInstance.put(
        `/user/update/${userId}`,
        editInfo,
        {
          headers: {
            Authorization: `Bearer ${userToken}`,
          },
        }
      );
      const dispatchResponse = dispatch(
        userActions.setUser(response.data.body)
      );
      dispatch(
        userActions.setRes({
          status: response.data.status,
          message: response.data.message,
        })
      );
      return dispatchResponse;
    } catch (error: any) {
      dispatch(
        userActions.setRes({
          status: error.response.data.status,
          message: error.response.data.message,
        })
      );
      return error;
    }
  };
};

const userThunks = { getUser, updateUser };

export default userThunks;
