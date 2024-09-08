import { useDispatch, useSelector } from "react-redux";
import { Chat, ChatUsers, GroofyWrapper } from "../../components";
import classes from "./scss/messaging.module.css";
import { chatThunks } from "../../store/actions";
import { useEffect } from "react";
const Messaging = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    const getUserChats = async () => {
      await dispatch(chatThunks.getAllUserChats() as any);
    };

    getUserChats();
  }, [dispatch]);

  return (
    <GroofyWrapper idx={-1}>
      <div className={classes.messaging_container}>
        <ChatUsers />
        <Chat type="user" />
      </div>
    </GroofyWrapper>
  );
};

export default Messaging;
