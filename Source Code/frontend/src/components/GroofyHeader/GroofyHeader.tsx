import { useState } from "react";
import { Link } from "react-router-dom";
import NotifyBox from "./NotifyBox/NotifyBox";
import "./scss/groofyheader.css";

const GroofyHeader = (probs: { idx: number }) => {
  const [notifyActive, setNotifyActive] = useState(false);
  const [notifyNewCnt, setNotifyNewCnt] = useState(15);
  const [notifyCnt, setNotifyCnt] = useState(4);
  const [profileActive, setProfileActive] = useState(false);
  return (
    <div className="header-container">
      <div className={`notify-area ${notifyActive}`}>
        <div className="notify-header">
          <h3 className="notify-title">Notifications</h3>
        </div>
        <div className="na-content">
          {notifyCnt > 0 ? (
            <NotifyBox
              nuImg="/Assets/Images/tourist.jpg"
              nusn="tourist"
              ntime="3 Minutes ago."
            />
          ) : (
            <div className="empty-box">
              <img
                src="/Assets/Images/empty_notify.png"
                alt="EmptyNotification"
              />
              <span>There are no notifications</span>
            </div>
          )}
        </div>
        <div className="notify-clear" onClick={() => setNotifyCnt(0)}>
          Clear all notifications
        </div>
      </div>
      <div className={`profile-area ${profileActive}`}>
        <Link to="/profile/1">
          <div className="pa-info">
            <img src="/Assets/Images/Hazem Adel.jpg" alt="ProfilePicture" />
            <span>Hazem Adel</span>
          </div>
        </Link>
        <div className="logout">
          <img src="/Assets/SVG/logout.svg" alt="ProfilePicture" />
          <span>Logout</span>
        </div>
      </div>
      <div className="header-logo">
        <Link to="/">
          <span>
            Groofy<span>Code</span>
          </span>
        </Link>
      </div>
      <ul className="header-nav-items">
        <Link to="/">
          <abbr title="Home">
            <li className={`${!probs.idx ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  !probs.idx ? "HomeIconColored" : "HomeIcon"
                }.svg`}
                alt=""
              />
            </li>
          </abbr>
        </Link>
        <Link to="/profile">
          <abbr title="Profile">
            <li className={`${probs.idx === 1 ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 1 ? "ProfileIconColored" : "ProfileIcon"
                }.svg`}
                alt=""
              />
            </li>
          </abbr>
        </Link>
        <Link to="/play">
          <abbr title="Play">
            <li className={`${probs.idx === 2 ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 2 ? "BattleIconColored" : "BattleIcon"
                }.svg`}
                alt=""
              />
            </li>
          </abbr>
        </Link>
        <Link to="/clan">
          <abbr title="Clan">
            <li className={`${probs.idx === 3 ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 3 ? "ClanIconColored" : "ClanIcon"
                }.svg`}
                alt=""
              />
            </li>
          </abbr>
        </Link>
        <Link to="/news">
          <abbr title="News">
            <li className={`${probs.idx === 4 ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 4 ? "NewsIconColored" : "NewsIcon"
                }.svg`}
                alt=""
              />
            </li>
          </abbr>
        </Link>
        <Link to="/help">
          <abbr title="Help">
            <li className={`${probs.idx === 5 ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 5 ? "HelpIconColored" : "HelpIcon"
                }.svg`}
                alt=""
              />
            </li>
          </abbr>
        </Link>
      </ul>
      <div className="header-user-area">
        <div className="header-h-imgbox">
          <div
            className="notify"
            onClick={() => {
              setNotifyActive((prev) => !prev);
              setProfileActive(false);
              setNotifyNewCnt(0);
            }}
          >
            {notifyNewCnt > 0 && (
              <div className="notify-cnt">
                <span>{notifyNewCnt}</span>
              </div>
            )}
            <img
              className="header-pr-notify"
              src="/Assets/SVG/notificationsIcon.svg"
              alt="notify"
            />
          </div>
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
