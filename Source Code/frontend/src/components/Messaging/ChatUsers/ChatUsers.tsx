import classes from "./scss/chatusers.module.css";
import { InputText } from "primereact/inputtext";
import { Badge } from "primereact/badge";
import { useDispatch, useSelector } from "react-redux";
import ProfileImage from "../../ProfileImage/ProfileImage";
import { chatThunks } from "../../../store/actions";

const ChatUsers = () => {
  const dispatch = useDispatch();
  const chats: any[] = useSelector((state: any) => state.chat.chats);

  console.log("CHATS: ", chats);

  const getUserChat = (userId: number, chatId: number) => {
    dispatch(chatThunks.getUserChat(userId, chatId) as any);
  };

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
        {chats.map((chat: any, idx: number) => (
          <div
            className={classes.chat_user}
            key={idx}
            onClick={() => {
              getUserChat(chat.receiverId, chat.id);
            }}
          >
            <div className={classes.chat_user_info}>
              <div className={classes.chat_user_img}>
                <ProfileImage
                  photoUrl={chat.receiverPhoto}
                  username={chat.receiverName}
                  style={{
                    backgroundColor: chat.receiverColor,
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
                <h3>{chat.receiverName}</h3>
                {/* <p>Are you here?</p> */}
              </div>
            </div>
            <div className={classes.chat_user_status}>
              <span>09:26 PM</span>
              {chat.unreadCount > 0 && (
                <Badge
                  className={classes.messages_cnt}
                  value={chat.unreadCount}
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
