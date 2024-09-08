import { reqInstance } from "..";
import { notifyActions } from "../slices/notify-slice";

const getAllNotifications = (page: Number = 0) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        const response = await reqInstance.get(`/notifications?p=${page}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(notifyActions.setNotifications(response.data.body));
        dispatch(notifyActions.setNotifyCnt(0));
      }
    } catch (error: any) {
      dispatch(notifyActions.setResponse(error.response.data));
    }
  };
};

const getNormalNotifications = (page: Number = 0) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/notifications/normal?p=${page}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(notifyActions.setNotifications(response.data.body));
        dispatch(notifyActions.setNotifyCnt(0));
      } catch (error: any) {
        dispatch(notifyActions.setResponse(error.response.data));
      }
    }
  };
};

const getFriendNotifications = (page: Number = 0) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/notifications/friend?p=${page}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(notifyActions.setNotifications(response.data.body));
        dispatch(notifyActions.setFriendNotifyCnt(0));
      } catch (error: any) {
        dispatch(notifyActions.setResponse(error.response.data));
      }
    }
  };
};

const socketNotification = (notification: any) => {
  return (dispatch: any) => {
    if (
      notification.notificationType === "FRIEND_REQUEST" ||
      notification.notificationType === "FRIEND_ACCEPT"
    ) {
      dispatch(notifyActions.setFriendNotifyCnt(notification.friendNotifyCnt));
    } else {
      dispatch(notifyActions.setNotifyCnt(notification.notifyCnt));
    }
  };
};

const removeNotification = (notificationId: Number) => {
  return (dispatch: any) => {
    dispatch(notifyActions.removeNotification(notificationId));
  };
};

const notifyThunks = {
  getAllNotifications,
  socketNotification,
  getNormalNotifications,
  getFriendNotifications,
  removeNotification,
};

export default notifyThunks;
