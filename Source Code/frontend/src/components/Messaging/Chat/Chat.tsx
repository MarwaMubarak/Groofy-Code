import { useEffect, useRef, useState } from "react";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { TieredMenu } from "primereact/tieredmenu";
import { MenuItem } from "primereact/menuitem";
import { ChatProps } from "../../../shared/types";
import classes from "./scss/chat.module.css";
import { useDispatch, useSelector } from "react-redux";
import { chatThunks } from "../../../store/actions";
import ProfileImage from "../../ProfileImage/ProfileImage";

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
  const dispatch = useDispatch();
  const loggedUser = useSelector((state: any) => state.auth.user);
  const stompClient = useSelector((state: any) => state.socket.stompClient);
  const clan = useSelector((state: any) => state.clan.clan);
  const messages: any[] = useSelector((state: any) => state.chat.messages);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const getClanChat = async () => {
      await dispatch(chatThunks.getClanChat(clan.id) as any);
    };

    getClanChat();
  }, [clan, dispatch]);

  console.log("Messages", messages);

  const sendMessage = () => {
    if (message.trim() === "") return;

    const data = {
      userId: loggedUser.id,
      content: message,
    };

    stompClient.send(
      `/app/clan/${clan.name}/sendMessage`,
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      JSON.stringify(data)
    );

    setMessage("");
  };

  function convertTo12HourFormat(isoString: any) {
    const date = new Date(isoString);

    // Extract components
    const year = date.getUTCFullYear();
    const month = (date.getUTCMonth() + 1).toString().padStart(2, "0");
    const day = date.getUTCDate().toString().padStart(2, "0");
    let hours = date.getUTCHours();
    const minutes = date.getUTCMinutes();

    // Determine AM or PM suffix
    const ampm = hours >= 12 ? "PM" : "AM";

    // Convert from 24-hour to 12-hour format
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'

    // Format minutes and seconds
    const minutesStr = minutes < 10 ? "0" + minutes : minutes;

    // Format the date and time
    const formattedDate = `${hours}:${minutesStr} ${ampm}`;

    return formattedDate;
  }

  return (
    <div className={classes.chat_container}>
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
        {messages.map((msg: any) =>
          loggedUser.id !== msg.userId ? (
            <div className={classes.msg_box + " " + classes.reciever}>
              <div className={classes.u_img}>
                {msg.photoUrl !== null ? (
                  <img src={msg.photoUrl} alt="profile_photo" />
                ) : (
                  <ProfileImage
                    photoUrl={msg.photoUrl}
                    username={msg.username}
                    style={{
                      backgroundColor: msg.accountColor,
                      width: "48px",
                      height: "48px",
                      fontSize: "24px",
                      marginBottom: "25px",
                    }}
                    canClick={false}
                  />
                )}
              </div>
              <div className={classes.msg_info}>
                <h4 className={classes.msg_usn}>{msg.username}</h4>
                <p className={classes.msg}>
                  {msg.content}
                  <span className={classes.message_date}>
                    {convertTo12HourFormat(msg.createdAt)}
                  </span>
                </p>
                {/* <span className={classes.message_status}>sent</span> */}
              </div>
            </div>
          ) : (
            <div className={classes.msg_box + " " + classes.sender}>
              <div className={classes.u_img}>
                {msg.photoUrl !== null ? (
                  <img src={msg.photoUrl} alt="profile_photo" />
                ) : (
                  <ProfileImage
                    photoUrl={msg.photoUrl}
                    username={msg.username}
                    style={{
                      backgroundColor: msg.accountColor,
                      width: "48px",
                      height: "48px",
                      fontSize: "24px",
                      marginBottom: "25px",
                    }}
                    canClick={false}
                  />
                )}
              </div>
              <div className={classes.msg_info}>
                <h4 className={classes.msg_usn}>{msg.username}</h4>
                <p className={classes.msg}>
                  {msg.content}
                  <span className={classes.message_date}>
                    {convertTo12HourFormat(msg.createdAt)}
                  </span>
                </p>
                {/* <span className={classes.message_status}>sent</span> */}
              </div>
            </div>
          )
        )}
      </div>
      <div className={classes.chat_footer}>
        <div className={classes.text_box}>
          <InputText
            className={classes.text_send}
            placeholder="Type a message..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />
          <div className={classes.chat_footer_actions}>
            <i className="bi bi-emoji-smile" />
            <i className="bi bi-paperclip" />
            <i className="bi bi-mic" />
          </div>
        </div>

        <Button
          className={`bi bi-send ${classes.send_text}`}
          onClick={sendMessage}
        />
      </div>
    </div>
  );
};

export default Chat;
