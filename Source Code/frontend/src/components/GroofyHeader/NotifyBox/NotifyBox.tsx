import { NotifyBoxProps } from "../../../shared/types";
import "./scss/notifybox.css";

const NotifyBox = (props: NotifyBoxProps) => {
  return (
    <div className="notify-box">
      <div className="notify-info">
        <div className="notify-img">
          <img src={props.nuImg} alt="user" />
        </div>
        <div className="notify-user">
          <span>
            <span>{props.nusn}</span> invited you to play a 1 vs 1 custom match.
          </span>
          <p>{props.ntime}</p>
        </div>
      </div>
      <div className="notify-btns">
        <img
          className="acc-btn"
          src="/Assets/SVG/acceptIcon.svg"
          alt="accept"
        />
        <img
          className="cancel-btn"
          src="/Assets/SVG/cancelIcon.svg"
          alt="cancel"
        />
      </div>
    </div>
  );
};

export default NotifyBox;
