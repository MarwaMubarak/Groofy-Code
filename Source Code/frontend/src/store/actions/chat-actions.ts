import { reqInstance } from "..";
import { chatActions } from "../slices/chat-slice";

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
        dispatch(chatActions.setChats(response.data.body));
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
        console.log("USER CHAT", response.data.body);
        const chatUser = {
          userId: response.data.body.receiverId,
          username: response.data.body.receiverName,
          photo: response.data.body.receiverPhoto,
          color: response.data.body.receiverColor,
        };

        dispatch(chatActions.setChatUser(chatUser));
        dispatch(chatActions.setUserMessages(response.data.body.messages));
        dispatch(chatActions.readUserMessages(chatId));
      } catch (err) {
        console.error("User Chat Error", err);
      }
    }
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

const chatThunks = {
  addClanMessage,
  addUserMessage,
  getClanChat,
  getAllUserChats,
  getUserChat,
};

export default chatThunks;
