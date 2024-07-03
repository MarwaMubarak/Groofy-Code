import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { chatThunks, gameThunks, popupThunks } from "../../store/actions";
import { notifyThunks, socketThunks } from "../../store/actions";

const WebSocketConnection = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const loggedUser = useSelector((state: any) => state.auth.user);

  useEffect(() => {
    if (loggedUser) {
      const socket = new SockJS("http://localhost:8080/socket");
      const client = Stomp.over(socket);

      client.connect(
        { Authorization: `Bearer ${localStorage.getItem("token")}` },
        function (frame: any) {
          client.subscribe(
            `/userTCP/${loggedUser.username}/notification`,
            onMessage,
            { Authorization: `Bearer ${localStorage.getItem("token")}` }
          );
          client.subscribe(
            `/userTCP/${loggedUser.username}/messages`,
            onChatMessage,
            { Authorization: `Bearer ${localStorage.getItem("token")}` }
          );
          client.subscribe(
            `/userTCP/${loggedUser.username}/games`,
            onGameMessage,
            { Authorization: `Bearer ${localStorage.getItem("token")}` }
          );
          client.subscribe(
            `/userTCP/${loggedUser.username}/code`,
            onCodeMessage,
            {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            }
          );
          if (loggedUser.clanName !== null) {
            client.subscribe(
              `/clanTCP/${loggedUser.clanName}/chat`,
              onChatMessage,
              {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
              }
            );
          }
        }
      );

      const onMessage = (message: any) => {
        const msg = JSON.parse(message.body);
        console.log("Recieved message: ", msg);

        if (msg.notificationType === "MATCH_INVITATION_ACCEPT") {
          console.log("Frie - Invitation accepted", msg);
          dispatch(gameThunks.gameNotify(true, false, msg.invitationId) as any);
        } else if (msg.notificationType === "MATCH_INVITATION_REJECT") {
          dispatch(gameThunks.gameNotify(false, true, null) as any);
        }

        dispatch(notifyThunks.socketNotification(msg) as any);
      };

      const onChatMessage = (message: any) => {
        const chatMsg = JSON.parse(message.body);
        console.log("Recieved chat message: ", chatMsg);
        if (chatMsg.messageType === "CLAN") {
          dispatch(chatThunks.addClanMessage(chatMsg) as any);
        } else {
          dispatch(chatThunks.addUserMessage(chatMsg.body) as any);
          dispatch(chatThunks.getAllUserChats() as any);
        }
      };

      const onGameMessage = (message: any) => {
        const msg = JSON.parse(message.body);
        console.log("Recieved game message: ", msg);
        if (msg.body && msg.body.id !== null) {
          dispatch(gameThunks.updateGroofyGame(msg.body.id) as any);
          dispatch(gameThunks.dismissToast() as any);
          navigate(`/game/${msg.body.id}`);
        } else {
          console.log("Frie - Match Finished", msg);
          dispatch(popupThunks.setPopUpState(true, msg) as any);
        }
      };

      const onCodeMessage = (message: any) => {
        dispatch(gameThunks.changeSubmitState(message.body) as any);
      };

      dispatch(socketThunks.changeStompClient(client) as any);
      return () => client.disconnect();
    }
  }, [dispatch, loggedUser, navigate]);

  return null;
};

export default WebSocketConnection;
