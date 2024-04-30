import { useRef, useEffect, useState } from "react";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { TieredMenu } from "primereact/tieredmenu";
import { MenuItem } from "primereact/menuitem";
import { ChatProps } from "../../../shared/types";
import classes from "./scss/chat.module.css";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const Chat = (props: ChatProps) => {
  const menu = useRef<any>(null);
  const items: MenuItem[] = [
    {
      label: "View Profile",
      icon: "pi pi-user",
    },
    {
      separator: true,
    },
    {
      label: "Mute",
      icon: "bi bi-bell-slash-fill",
      items: [
        {
          label: "For 15 minutes",
        },
        {
          label: "For 1 hour",
        },
        {
          label: "For 8 hours",
        },
        {
          label: "For 24 hours",
        },
        {
          label: "Until I turn it back on",
        },
      ],
    },

    {
      label: "Unfriend",
      icon: "bi bi-person-fill-x",
      className: `${classes.unfriend}`,
    },
    {
      label: "Block",
      icon: "bi bi-ban",
    },
  ];

  const [messages, setMessages] = useState<any>([]);
  const [stompClient, setStompClient] = useState<any>(null);

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/socket");
    const client = Stomp.over(socket);
    // client.debug = () => {};

    client.connect(
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      function (frame: any) {
        client.subscribe("/topic/return-to", function (message) {
          setMessages((prev: any) => [...prev, message.body]);
        });
      }
    );

    setStompClient(client as any);
    return () => client.disconnect();
  }, []);

  const sendMessage = (msg: any) => {
    stompClient.send(
      "/app/message",
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      "Hello, world!"
    );
  };

  return (
    <div className={classes.chat_container}>
      <button onClick={sendMessage}></button>
      <TieredMenu
        model={items}
        popup
        ref={menu}
        breakpoint="767px"
        className={classes.dots_menu}
      />
      {props.type === "clan" ? (
        <div className={classes.clan_chat_header}>
          <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
          <span>Chat</span>
        </div>
      ) : (
        <div className={classes.chat_header}>
          <div className={classes.friend_box}>
            <div className={classes.friend_img}>
              <img src="/Assets/Images/Hazem Adel.jpg" alt="FriendProfilePic" />
            </div>
            <div className={classes.friend_info}>
              <h3>Hazem Adel</h3>
              <p>Online</p>
            </div>
          </div>
          <div
            className={classes.header_dots}
            onClick={(e) => menu.current.toggle(e)}
          >
            <i className="bi bi-three-dots" />
          </div>
        </div>
      )}
      <div className={classes.chat_body}>
        <span className={classes.chat_date}>Today</span>
        <div className={classes.msg_box + " " + classes.reciever}>
          <div className={classes.u_img}>
            <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
          </div>
          <div className={classes.msg_info}>
            <h4 className={classes.msg_usn}>Hazem Adel</h4>
            <p className={classes.msg}>
              This is a test message, not an online one.
              <span className={classes.message_date}>09:21 AM</span>
            </p>
            <span className={classes.message_status}>sent</span>
          </div>
        </div>
        <div className={classes.msg_box + " " + classes.sender}>
          <div className={classes.u_img}>
            <img src="/Assets/Images/tourist.jpg" alt="profilePhoto" />
          </div>
          <div className={classes.msg_info}>
            <h4 className={classes.msg_usn}>Tourist</h4>
            <p className={classes.msg}>
              This is a test response, not an online one.
              <span className={classes.message_date}>09:21 AM</span>
            </p>
            <span className={classes.message_status}>sent</span>
          </div>
        </div>
      </div>
      <div className={classes.chat_footer}>
        <div className={classes.text_box}>
          <InputText
            className={classes.text_send}
            placeholder="Type a message..."
          />
          <div className={classes.chat_footer_actions}>
            <i className="bi bi-emoji-smile" />
            <i className="bi bi-paperclip" />
            <i className="bi bi-mic" />
          </div>
        </div>

        <Button className={`bi bi-send ${classes.send_text}`} />
      </div>
    </div>
  );
};

export default Chat;
