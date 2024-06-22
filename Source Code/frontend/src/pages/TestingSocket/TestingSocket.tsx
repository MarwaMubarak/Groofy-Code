import { useState, useEffect } from "react";
import toast, { Toaster } from "react-hot-toast";
import { useSelector } from "react-redux";
import classes from "./scss/testing-socket.module.css";
// import { Stomp } from "@stomp/stompjs";
// import SockJS from "sockjs-client";

const TestingSocket = () => {
  const [messages, setMessages] = useState<any>([]);
  const stompClient = useSelector((state: any) => state.socket.stompClient);
  const loggedUser = useSelector((state: any) => state.auth.user);
  // useEffect(() => {
  //   const socket = new SockJS("http://localhost:8080/socket");
  //   const client = Stomp.over(socket);
  //   // client.debug = () => {};

  //   client.connect(
  //     { Authorization: `Bearer ${localStorage.getItem("token")}` },
  //     function (frame: any) {
  //       // client.subscribe("/clans", onMessage, { Authorization: `Bearer ${localStorage.getItem("token")}` });
  //       // client.subscribe(`/user`, onMessage);
  //       client.subscribe(
  //         `/userTCP/${loggedUser.username}/notification`,
  //         onMessage,
  //         { Authorization: `Bearer ${localStorage.getItem("token")}` }
  //       );
  //       client.subscribe(`/clanTCP/testClan/chat`, onMessage, {
  //         Authorization: `Bearer ${localStorage.getItem("token")}`,
  //       });
  //     }
  //   );
  //   const onMessage = (message: any) => {
  //     // console.log("Message", message.body);
  //     console.log("Message", JSON.parse(message.body));
  //   };

  //   setStompClient(client as any);
  //   return () => client.disconnect();
  // }, [loggedUser.username]);

  const sendMessage = (clanName: string, msg: any) => {
    const data = {
      userId: loggedUser.id,
      content: msg,
    };
    stompClient.send(
      `/app/clan/${clanName}/sendMessage`,
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      JSON.stringify(data)
    );
  };

  const getToast = () =>
    toast.custom(
      <div className={classes.toast_testing}>
        <img src="./Assets/Images/Hazem Adel.jpg" alt="ProfilePicture" />
        <span>Hello</span>
      </div>
    );

  const getToast2 = () => toast.success("Hello");

  return (
    <div>
      <button onClick={() => sendMessage(loggedUser.clanName, "Hello")}>
        Send
      </button>
      <br />
      <br />
      <button onClick={getToast}>Toast 1</button>
      <br />
      <br />
      <button onClick={getToast2}>Toast 2</button>
      <Toaster />
    </div>
  );
};

export default TestingSocket;
