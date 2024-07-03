import { SimpleUserProps } from "../../shared/types";
import ProfileImage from "../ProfileImage/ProfileImage";
import classes from "./scss/simple-user.module.css";

const SimpleUser = (props: SimpleUserProps) => {
  return (
    <div
      className={`${classes.simple_user} ${
        props.reverse === true && classes.rev
      }`}
    >
      <ProfileImage
        photoUrl={props.photoUrl}
        username={props.username}
        style={{
          backgroundColor: props.accountColor,
          width: "40px",
          height: "40px",
          fontSize: "18px",
          marginRight: "10px",
          cursor: "pointer",
        }}
        canClick={false}
      />
      <span className={classes.username}>{props.username}</span>
    </div>
  );
};

export default SimpleUser;
