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
import Picker from "emoji-picker-react";
import EmojiPicker from "emoji-picker-react";

const Chat = (props: ChatProps) => {
  const [showPicker, setShowPicker] = useState(false);

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
  const chatUser = useSelector((state: any) => state.chat.chatUser);
  const clan = useSelector((state: any) => state.clan.clan);
  const chatId = useSelector((state: any) => state.chat.chatId);
  const clanMessages: any[] = useSelector(
    (state: any) => state.chat.clanMessages
  );
  const userMessages: any[] = useSelector(
    (state: any) => state.chat.userMessages
  );
  const [message, setMessage] = useState("");

  useEffect(() => {
    const getClanChat = async () => {
      if (props.type === "clan") {
        await dispatch(chatThunks.getClanChat(clan.id) as any);
      }
    };

    getClanChat();
  }, [clan, dispatch, props.type]);

  const readChatMessages = () => {
    dispatch(chatThunks.readChatMessages(chatId) as any);
  };

  const sendMessage = () => {
    if (message.trim() === "") return;

    const data = {
      userId: loggedUser.id,
      content: message,
    };

    stompClient.send(
      `/app/${props.type}/${
        props.type === "clan" ? clan.name : chatUser.username
      }/sendMessage`,
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      JSON.stringify(data)
    );

    setMessage("");
  };

  function convertTo12HourFormat(isoString: string): string {
    const date = new Date(isoString);
    const now = new Date();

    // Extract components using local time
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    let hours = date.getHours();
    const minutes = date.getMinutes();

    // Determine AM or PM suffix
    const ampm = hours >= 12 ? "PM" : "AM";

    // Convert from 24-hour to 12-hour format
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'

    // Format minutes
    const minutesStr = minutes < 10 ? "0" + minutes : minutes;

    // Calculate difference in days
    const oneDayInMs = 24 * 60 * 60 * 1000;
    const diffInMs = now.setHours(0, 0, 0, 0) - date.setHours(0, 0, 0, 0);
    const diffInDays = diffInMs / oneDayInMs;

    // Determine if the date is today, yesterday, or another day
    let dateStr: string;
    if (diffInDays === 0) {
      dateStr = "Today";
    } else if (diffInDays === 1) {
      dateStr = "Yesterday";
    } else {
      const daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
      const monthsOfYear = [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
      ];
      const dayOfWeek = daysOfWeek[date.getDay()];
      const monthStr = monthsOfYear[date.getMonth()];
      dateStr = `${dayOfWeek}, ${monthStr} ${day}`;
    }

    // Format the date and time
    const formattedDate = `${dateStr} ${hours}:${minutesStr} ${ampm}`;

    return formattedDate;
  }

  const onEmojiClick = (event: any) => {
    console.log("EVeeent", event);
    setMessage((msg) => msg + event.emoji);
  };

  return (
    <div className={classes.chat_container}>
      {props.type === "clan" || chatUser !== null ? (
        <>
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
                  <ProfileImage
                    photoUrl={chatUser.photo}
                    username={chatUser.username}
                    style={{
                      backgroundColor: chatUser.color,
                      width: "50px",
                      height: "50px",
                      fontSize: "18px",
                      marginRight: "10px",
                    }}
                    canClick={false}
                  />
                </div>
                <div className={classes.friend_info}>
                  <h3>{chatUser.username}</h3>
                  {/* <p>Online</p> */}
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
            {props.type === "clan"
              ? clanMessages?.map((msg: any, idx: number) =>
                  loggedUser.id !== msg.userId ? (
                    <div
                      className={classes.msg_box + " " + classes.reciever}
                      key={idx}
                    >
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
                    <div
                      className={classes.msg_box + " " + classes.sender}
                      key={idx}
                    >
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
                )
              : userMessages?.map((msg: any, idx: number) =>
                  loggedUser.id !== msg.userId ? (
                    <div
                      className={classes.msg_box + " " + classes.reciever}
                      key={idx}
                    >
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
                    <div
                      className={classes.msg_box + " " + classes.sender}
                      key={idx}
                    >
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
                onFocus={readChatMessages}
              />
              <div className={classes.chat_footer_actions}>
                <i
                  className="bi bi-emoji-smile"
                  onClick={() => setShowPicker((val) => !val)}
                  style={{ position: "relative" }}
                ></i>
                {showPicker && (
                  <Picker
                    style={{
                      width: "400px",
                      position: "absolute",
                      bottom: "50px",
                      right: "-30px",
                    }}
                    onEmojiClick={onEmojiClick}
                  />
                )}
              </div>
            </div>

            <Button
              className={`bi bi-send ${classes.send_text}`}
              onClick={sendMessage}
            />
          </div>
        </>
      ) : (
        <span>Start a new conversation to see it here</span>
      )}
    </div>
  );
};

export default Chat;
