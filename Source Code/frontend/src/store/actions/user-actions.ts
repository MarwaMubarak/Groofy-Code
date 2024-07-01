import { reqInstance } from "..";
import { authActions } from "../slices/auth-slice";
import { notifyActions } from "../slices/notify-slice";
import { userActions } from "../slices/user-slice";

export interface EditInfo {
  displayName: string;
  bio: string;
  country: string;
}

const getUser = (username: string) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    try {
      const response = await reqInstance.get(`/users/${username}`, {
        headers: {
          Authorization: `Bearer ${token}`,
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

const getProfile = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/users/profile`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(authActions.setUser(response.data.body));
        dispatch(
          notifyActions.setNotificationsCnt({
            notifyCnt: response.data.body.notifyCnt,
            messageNotifyCnt: response.data.body.messageNotifyCnt,
            friendNotifyCnt: response.data.body.friendNotifyCnt,
          })
        );
      } catch (error: any) {
        throw error;
      }
    }
  };
};

const updateUser = (editInfo: EditInfo) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      const response = await reqInstance.put(`/users`, editInfo, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const newInfo = { ...response.data.body };
      dispatch(authActions.setUser(newInfo));
      return response;
    } catch (error: any) {
      return error;
    }
  };
};

const changePhoto = (userPhoto: File | null) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        if (userPhoto !== null) {
          dispatch(authActions.setIsUploading(true));
        } else {
          dispatch(authActions.setIsDeleting(true));
        }
        const formData = new FormData();
        if (userPhoto !== null) {
          formData.append("user_photo", userPhoto);
        }
        const response = await reqInstance.post(
          `/user/change-photo`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "multipart/form-data",
            },
          }
        );
        console.log("Response: ", response);
        dispatch(authActions.updateUserPhoto(response.data.body));
        if (userPhoto !== null) {
          dispatch(authActions.setIsUploading(false));
        } else {
          dispatch(authActions.setIsDeleting(false));
        }
        return response.data;
      } catch (error: any) {
        console.log("Error: ", error);
        if (userPhoto !== null) {
          dispatch(authActions.setIsUploading(false));
        } else {
          dispatch(authActions.setIsDeleting(false));
        }
        throw error.response.data;
      }
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
      const token = localStorage.getItem("token");
      const response = await reqInstance.put(
        `/users/password`,
        {
          currentPassword: currentPassword,
          password: password,
          confirmPassword: confirmPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
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
      const token = localStorage.getItem("token");
      const response = await reqInstance.get(`/users/search/${searchQuery}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response;
    } catch (error: any) {
      return error;
    }
  };
};

const userThunks = {
  getUser,
  getProfile,
  updateUser,
  changePhoto,
  changePassword,
  searchForUsers,
};

export default userThunks;
