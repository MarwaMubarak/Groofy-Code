import { useState, useEffect } from "react";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const TestingSocket = () => {
  const [messages, setMessages] = useState<any>([]);
  const [stompClient, setStompClient] = useState<any>(null);

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/socket");
    const client = Stomp.over(socket);
    // client.debug = () => {};

    client.connect(
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      function (frame: any) {
        client.subscribe("/clan", onMessage);
        // client.subscribe(`/user`, onMessage);
        client.subscribe(`/clan/hazemadel/asd`, onMessage);
      }
    );
    const onMessage = (message: any) => {
      console.log("Message", JSON.parse(message.body));
    };

    setStompClient(client as any);
    return () => client.disconnect();
  }, []);

  const sendMessage = (clanId: any, msg: any) => {
    const data = {
      userId: 2,
      content: msg,
    };
    stompClient.send(
      `/app/clan/${clanId}/sendMessage`,
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      JSON.stringify(data)
    );
  };
  return (
    <div>
      <button onClick={() => sendMessage(1, "Hello")}>Send</button>
    </div>
  );
};

export default TestingSocket;
