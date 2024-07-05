import { reqInstance } from "..";
import { chatActions } from "../slices/chat-slice";
import { notifyActions } from "../slices/notify-slice";

const getClanChat = (clanId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/chats/clan/${clanId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(chatActions.setClanMessages(response.data.body.messages));
      } catch (err) {
        console.error("Clan Chat Error", err);
      }
    }
  };
};

const getAllUserChats = (page: number = 0) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/chats/chat?p=${page}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(chatActions.setChats(response.data.body.chats));
        dispatch(
          notifyActions.setMessageNotifyCnt(response.data.body.unreadChatsCount)
        );
      } catch (err) {
        console.error("User Chat Error", err);
      }
    }
  };
};

const getUserChat = (userId: number, chatId: number, page: number = 0) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/chats/chat/${userId}?p=${page}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (response.data.body.unreadCount > 0) {
          dispatch(notifyActions.openUserChat());
        }
        const chatUser = {
          userId: response.data.body.receiverId,
          username: response.data.body.receiverName,
          photo: response.data.body.receiverPhoto,
          color: response.data.body.receiverColor,
        };

        dispatch(chatActions.setChatUser(chatUser));
        dispatch(chatActions.setUserMessages(response.data.body.messages));
        dispatch(chatActions.readUserMessages(chatId));
        dispatch(chatActions.setChatId(chatId));
      } catch (err) {
        console.error("User Chat Error", err);
      }
    }
  };
};

const clearChat = () => {
  return (dispatch: any) => {
    dispatch(chatActions.clearChat());
  };
};

const addClanMessage = (message: any) => {
  return (dispatch: any) => {
    dispatch(chatActions.addClanMessage(message));
  };
};

const addUserMessage = (message: any) => {
  return (dispatch: any) => {
    dispatch(chatActions.addUserMessage(message));
  };
};

const readChatMessages = (chatId: number) => {
  return (dispatch: any) => {
    dispatch(chatActions.readUserMessages(chatId));
  };
};

const chatThunks = {
  addClanMessage,
  addUserMessage,
  getClanChat,
  getAllUserChats,
  getUserChat,
  clearChat,
  readChatMessages,
};

export default chatThunks;
