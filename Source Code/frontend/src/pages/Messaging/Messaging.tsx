import React from "react";
import { Chat, ChatUsers, GroofyWrapper } from "../../components";
import classes from "./scss/messaging.module.css";
const Messaging = () => {
  return (
    <GroofyWrapper idx={-1}>
      <div className={classes.messaging_container}>
        <ChatUsers />
        <Chat />
      </div>
    </GroofyWrapper>
  );
};

export default Messaging;
