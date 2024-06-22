import { reqInstance } from "..";
import { notifyActions } from "../slices/notify-slice";

const getAllNotifications = (page?: Number) => {
  if (page === undefined) page = 0;
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

const socketNotification = (notification: any) => {
  return (dispatch: any) => {
    dispatch(notifyActions.setNotifyCnt(notification.notifyCnt));
  };
};

const notifyThunks = { getAllNotifications, socketNotification };

export default notifyThunks;
