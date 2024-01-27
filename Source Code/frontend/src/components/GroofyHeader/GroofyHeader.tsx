import { useState } from "react";
import { useNavigate } from "react-router-dom";
import NotifyBox from "./NotifyBox/NotifyBox";
import "./scss/groofyheader.css";
import ActionButton from "./ActionButton/ActionButton";
import { useDispatch } from "react-redux";
import { Link } from "react-router-dom";
import { authThunks } from "../../store/actions";

const GroofyHeader = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [notifyActive, setNotifyActive] = useState(false);
  const [notifyNewCnt, setNotifyNewCnt] = useState(15);
  const [notifyCnt, setNotifyCnt] = useState(4);

  const [messageActive, setMessageActive] = useState(false);
  const [messageNewCnt, setMessageNewCnt] = useState(3);

  const [friendsActive, setFriendsActive] = useState(false);
  const [friendsNewCnt, setFriendsNewCnt] = useState(7);

  const [profileActive, setProfileActive] = useState(false);

  const handleLogout = () => {
    // @ts-ignore
    dispatch(authThunks.logout());
    navigate("/login");
  };
  return (
    <div className="header-container">
      <div className={`notify-area ${notifyActive}`}>
        <div className="notify-header">
          <h3 className="notify-title">Notifications</h3>
          <abbr title="Clear notifications">
            <div className="clear-notify" onClick={() => setNotifyCnt(0)}>
              <img src="./Assets/SVG/clear.svg" alt="Clear" />
            </div>
          </abbr>
        </div>
        <div className="na-content">
          {notifyCnt > 0 ? (
            <NotifyBox
              nuImg="./Assets/Images/tourist.jpg"
              nusn="tourist"
              ntime="3 Minutes ago."
            />
          ) : (
            <div className="empty-box">
              <img
                src="./Assets/Images/empty_notify.png"
                alt="EmptyNotification"
              />
              <span>There are no notifications</span>
            </div>
          )}
        </div>
        <div className="see-all">See more notifications</div>
      </div>
      <div className={`profile-area ${profileActive}`}>
        <div className="pa-info">
          <img src="/Assets/Images/Hazem Adel.jpg" alt="ProfilePicture" />
          <span>Hazem Adel</span>
        </div>
        <Link to="/profile">
          <div className="logout">
            <img src="/Assets/SVG/ProfileIconBlack.svg" alt="ProfilePicture" />
            <span>Profile</span>
          </div>
        </Link>
        <Link to="/settings">
          <div className="logout">
            <img src="/Assets/SVG/settings.svg" alt="ProfilePicture" />
            <span>Settings</span>
          </div>
        </Link>
        <div className="logout" onClick={handleLogout}>
          <img src="/Assets/SVG/logout.svg" alt="ProfilePicture" />
          <span>Logout</span>
        </div>
      </div>
      <div className="header-logo">
        <Link to="/">
          <img src="/Assets/Images/GroofyLogoCover.png" />
          {/* <span>
            Groofy<span>Code</span>
          </span> */}
        </Link>
      </div>
      <div className="header-user-area">
        <div className="header-h-imgbox">
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
            className="pr-ph"
            onClick={() => {
              setProfileActive((state) => !state);
              setNotifyActive(false);
            }}
          >
            <img
              className="header-pr-ph"
              src="/Assets/Images/Hazem Adel.jpg"
              alt="ProfilePhoto"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default GroofyHeader;
