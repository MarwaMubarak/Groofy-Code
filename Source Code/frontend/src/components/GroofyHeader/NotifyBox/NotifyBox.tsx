import { ProfileImage } from "../..";
import { NotifyBoxProps } from "../../../shared/types";
import classes from "./scss/notifybox.module.css";

const NotifyBox = (props: NotifyBoxProps) => {
  return (
    <div className={classes.notify_box}>
      <div className={classes.notify_info}>
        <div className={classes.notify_img}>
          <ProfileImage
            photoUrl={props.nuImg}
            username={props.nusn}
            style={{
              cursor: "pointer",
              backgroundColor: props.ncolor,
              width: "45px",
              height: "45px",
              fontSize: "20px",
            }}
            canClick={false}
          />
        </div>
        <div className={classes.notify_user}>
          <span>
            <span>{props.nusn}</span> {props.nbody}
          </span>
          <p>{props.ntime}</p>
        </div>
      </div>
      {props.nType !== "SEND_LIKE" && (
        <div className={classes.notify_btns}>
          <img
            className={classes.acc_btn}
            src="/Assets/SVG/acceptIcon.svg"
            alt="accept"
          />
          <img
            className={classes.cancel_btn}
            src="/Assets/SVG/cancelIcon.svg"
            alt="cancel"
          />
        </div>
      )}
    </div>
  );
};

export default NotifyBox;
