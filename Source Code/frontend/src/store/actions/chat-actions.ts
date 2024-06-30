import { reqInstance } from "..";
import { chatActions } from "../slices/chat-slice";

const getClanChat = (clanId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/chats/${clanId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(chatActions.setMessages(response.data.body.messages));
      } catch (err) {
        console.error("Clan Chat Error", err);
      }
    }
  };
};

const addMessage = (message: any) => {
  return (dispatch: any) => {
    dispatch(chatActions.addMessage(message));
  };
};

const chatThunks = { addMessage, getClanChat };

export default chatThunks;
