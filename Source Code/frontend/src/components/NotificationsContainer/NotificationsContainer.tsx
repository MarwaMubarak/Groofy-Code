import FormatDate from "../../shared/functions/format-date";
import classes from "./scss/notification-container.module.css";
import NotifyBox from "../GroofyHeader/NotifyBox/NotifyBox";
import { NotificationsContainerProps } from "../../shared/types";
import { useRef } from "react";
import useClickOutside from "../../shared/functions/handleClickOutside";

const NotificationsContainer = (props: NotificationsContainerProps) => {
  const componentRefNotifyArea = useRef(null);

  useClickOutside(componentRefNotifyArea, () => {
    props.setNotifyActive(false);
  });

  return (
    <div
      className={`${classes.notify_area} ${
        props.isActive ? classes.true : classes.false
      }`}
      ref={componentRefNotifyArea}
      style={{ right: props.rightPosition }}
    >
      <div className={classes.notify_header}>
        <h3 className={classes.notify_title}>{props.title}</h3>
        <abbr title={`Clear ${props.title}`}>
          <div
            className={classes.clear_notify}
            onClick={() => props.setNotifyCnt(0)}
          >
            <img src="/Assets/SVG/clear.svg" alt="Clear" />
          </div>
        </abbr>
      </div>
      <div className={classes.na_content}>
        {props.notifications.length > 0 ? (
          props.notifications.map((notify, idx) => (
            <NotifyBox
              key={idx}
              nuImg={notify.img}
              ncolor={notify.color}
              nusn={notify.sender}
              nbody={notify.body}
              ntime={FormatDate(notify.createdAt) ?? "Some time ago"}
              nType={notify.notificationType}
            />
          ))
        ) : (
          <div className={classes.empty_box}>
            <img
              src="/Assets/Images/empty_notify.png"
              alt="EmptyNotification"
            />
            <span>{props.desc}</span>
          </div>
        )}
      </div>
      <div className={classes.see_all}>See more</div>
    </div>
  );
};

export default NotificationsContainer;
