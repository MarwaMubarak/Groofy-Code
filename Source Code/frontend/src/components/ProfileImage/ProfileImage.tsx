import { ProfileImageProps } from "../../shared/types";
import classes from "./scss/profile-image.module.css";

const ProfileImage = (props: ProfileImageProps) => {
  return (
    <div
      className={classes.pr_ph}
      onClick={() => {
        if (props.canClick) {
          props.exec();
        }
      }}
    >
      {props.photoUrl ? (
        <img
          className={`${classes.header_pr_ph} ${
            props.canClick && classes.can_click
          }`}
          style={props.style}
          src={props.photoUrl}
          alt="ProfilePhoto"
        />
      ) : (
        <div
          className={`${classes.account_color} ${
            props.canClick && classes.can_click
          }`}
          style={props.style}
        >
          <span>{props.username.charAt(0).toUpperCase()}</span>
        </div>
      )}
    </div>
  );
};

export default ProfileImage;
