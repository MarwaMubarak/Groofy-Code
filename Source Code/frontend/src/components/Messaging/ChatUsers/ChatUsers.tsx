import classes from "./scss/chatusers.module.css";
import { InputText } from "primereact/inputtext";
import { Badge } from "primereact/badge";
import { useDispatch, useSelector } from "react-redux";
import ProfileImage from "../../ProfileImage/ProfileImage";
import { chatThunks } from "../../../store/actions";
import { useEffect } from "react";

const ChatUsers = () => {
  const dispatch = useDispatch();
  const chats: any[] = useSelector((state: any) => state.chat.chats);

  console.log("CHATS: ", chats);

  useEffect(() => {
    dispatch(chatThunks.clearChat() as any);
  }, [dispatch]);

  const getUserChat = (userId: number, chatId: number) => {
    dispatch(chatThunks.getUserChat(userId, chatId) as any);
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

  return (
    <div className={classes.chats_container}>
      <div className={classes.chats_header}>
        <h3>Messages</h3>
        <i className="bi bi-pencil-square" />
      </div>
      <div className={classes.chats_search}>
        <InputText className={classes.search_input} placeholder="Search" />
        <i className="bi bi-search"></i>
      </div>
      <div className={classes.chats_users}>
        {chats.map((InfoChat: any, idx: number) => (
          <div
            className={classes.chat_user}
            key={idx}
            onClick={() => {
              getUserChat(InfoChat.chat.receiverId, InfoChat.chat.id);
            }}
          >
            <div className={classes.chat_user_info}>
              <div className={classes.chat_user_img}>
                <ProfileImage
                  photoUrl={InfoChat.chat.receiverPhoto}
                  username={InfoChat.chat.receiverName}
                  style={{
                    backgroundColor: InfoChat.chat.receiverColor,
                    width: "50px",
                    height: "50px",
                    fontSize: "18px",
                    marginRight: "10px",
                  }}
                  canClick={false}
                />

                {/* <div className={classes.online}></div> */}
              </div>
              <div className={classes.chat_user_data}>
                <h3>{InfoChat.chat.receiverName}</h3>
                {/* <p>Are you here?</p> */}
              </div>
            </div>
            <div className={classes.chat_user_status}>
              <span>{convertTo12HourFormat(InfoChat.lastMessageDate)}</span>
              {InfoChat.chat.unreadCount > 0 && (
                <Badge
                  className={classes.messages_cnt}
                  value={InfoChat.chat.unreadCount}
                />
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ChatUsers;
