import { useState } from "react";
import { useNavigate } from "react-router-dom";
import NotifyBox from "./NotifyBox/NotifyBox";
import ActionButton from "./ActionButton/ActionButton";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { authThunks } from "../../store/actions";
import { postActions } from "../../store/slices/post-slice";
import classes from "./scss/groofyheader.module.css";

const GroofyHeader = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state: any) => state.auth.user);
  const [notifyActive, setNotifyActive] = useState(false);
  const [notifyNewCnt, setNotifyNewCnt] = useState(15);
  const [notifyCnt, setNotifyCnt] = useState(4);

  const [messageActive, setMessageActive] = useState(false);
  const [messageNewCnt, setMessageNewCnt] = useState(3);

  const [friendsActive, setFriendsActive] = useState(false);
  const [friendsNewCnt, setFriendsNewCnt] = useState(7);

  const [profileActive, setProfileActive] = useState(false);

  const handleLogout = () => {
    dispatch(authThunks.logout() as any);
    dispatch(postActions.setStatus(""));
    dispatch(postActions.setMessage(""));
    navigate("/login");
  };
  return (
    <div className={classes.header_container}>
      <div
        className={`${classes.notify_area} ${
          notifyActive ? classes.true : classes.false
        }`}
      >
        <div className={classes.notify_header}>
          <h3 className={classes.notify_title}>Notifications</h3>
          <abbr title="Clear notifications">
            <div
              className={classes.clear_notify}
              onClick={() => setNotifyCnt(0)}
            >
              <img src="/Assets/SVG/clear.svg" alt="Clear" />
            </div>
          </abbr>
        </div>
        <div className={classes.na_content}>
          {notifyCnt > 0 ? (
            <NotifyBox
              nuImg="/Assets/Images/tourist.jpg"
              nusn="tourist"
              ntime="3 Minutes ago."
            />
          ) : (
            <div className={classes.empty_box}>
              <img
                src="/Assets/Images/empty_notify.png"
                alt="EmptyNotification"
              />
              <span>There are no notifications</span>
            </div>
          )}
        </div>
        <div className={classes.see_all}>See more notifications</div>
      </div>
      <div
        className={`${classes.profile_area} ${
          profileActive ? classes.true : classes.false
        }`}
      >
        <div className={classes.pa_info}>
          <img src={user.photo.url} alt="ProfilePicture" />
          <span>{user.username}</span>
        </div>
        <Link to={`/profile/${user.username}`}>
          <div className={classes.pa_menu}>
            <img src="/Assets/SVG/ProfileIconBlack.svg" alt="ProfilePicture" />
            <span>Profile</span>
          </div>
        </Link>
        <Link to="/settings">
          <div className={classes.pa_menu}>
            <img src="/Assets/SVG/settings.svg" alt="ProfilePicture" />
            <span>Settings</span>
          </div>
        </Link>
        <div className={classes.pa_menu} onClick={handleLogout}>
          <img src="/Assets/SVG/logout.svg" alt="ProfilePicture" />
          <span>Logout</span>
        </div>
      </div>
      <div className={classes.header_logo}>
        <Link to="/">
          <img src="/Assets/Images/GroofyLogoCover.png" alt="Logo" />
        </Link>
      </div>
      <div className={classes.header_user_area}>
        <div className={classes.header_h_imgbox}>
          <ActionButton
            count={friendsNewCnt}
            img="/Assets/SVG/people.svg"
            clickEvent={() => {
              setFriendsActive((prev) => !prev);
              setProfileActive(false);
              setFriendsNewCnt(0);
            }}
          />
          <ActionButton
            count={messageNewCnt}
            img="/Assets/SVG/message.svg"
            clickEvent={() => {
              setMessageActive((prev) => !prev);
              setProfileActive(false);
              setMessageNewCnt(0);
            }}
          />
          <ActionButton
            count={notifyNewCnt}
            img="/Assets/SVG/notificationsIcon.svg"
            clickEvent={() => {
              setNotifyActive((prev) => !prev);
              setProfileActive(false);
              setNotifyNewCnt(0);
            }}
          />

          <div
            className={classes.pr_ph}
            onClick={() => {
              setProfileActive((state) => !state);
              setNotifyActive(false);
            }}
          >
            <img
              className={classes.header_pr_ph}
              src={user.photo.url}
              alt="ProfilePhoto"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default GroofyHeader;
