import { NotifyBoxProps } from "../../../shared/types";
import classes from "./scss/notifybox.module.css";

const NotifyBox = (props: NotifyBoxProps) => {
  return (
    <div className={classes.notify_box}>
      <div className={classes.notify_info}>
        <div className={classes.notify_img}>
          <img src={props.nuImg} alt="user" />
        </div>
        <div className={classes.notify_user}>
          <span>
            <span>{props.nusn}</span> invited you to play a 1 vs 1 custom match.
          </span>
          <p>{props.ntime}</p>
        </div>
      </div>
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
    </div>
  );
};

export default NotifyBox;
