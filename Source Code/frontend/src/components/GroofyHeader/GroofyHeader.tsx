import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import NotifyBox from "./NotifyBox/NotifyBox";
import ActionButton from "./ActionButton/ActionButton";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import {
  authThunks,
  gameThunks,
  notifyThunks,
  userThunks,
} from "../../store/actions";
import { postActions } from "../../store/slices/post-slice";
import classes from "./scss/groofyheader.module.css";
import { OverlayPanel } from "primereact/overlaypanel";
import { AxiosError } from "axios";
import styles from "./scss/overlay.module.css";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { ProgressSpinner } from "primereact/progressspinner";
import ReactCountryFlag from "react-country-flag";
import BurgerMenu from "../BurgerMenu/BurgerMenu";
import useClickOutside from "../../shared/functions/handleClickOutside";
import ProfileImage from "../ProfileImage/ProfileImage";
import FormatDate from "../../shared/functions/format-date";
import NotificationsContainer from "../NotificationsContainer/NotificationsContainer";

interface Country {
  name: string;
  code: string;
}

const GroofyHeader = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const loggedUser = useSelector((state: any) => state.auth.user);
  const notifications: any[] = useSelector(
    (state: any) => state.notify.notifications
  );

  const normalNotifyCnt = useSelector((state: any) => state.notify.notifyCnt);
  const messageNotifyCnt = useSelector(
    (state: any) => state.notify.messageNotifyCnt
  );
  const friendNotifyCnt = useSelector(
    (state: any) => state.notify.friendNotifyCnt
  );

  const op = useRef<OverlayPanel>(null);
  const [searchText, setSearchText] = useState<string>("");
  const [counterToFetch, setCounterToFetch] = useState<number>(2);
  const [searchedUsers, setSearchedUsers] = useState<any[]>([]);
  const componentRefProfileArea = useRef(null);
  const componentRefNotifyArea = useRef(null);
  const [searchActive, setSearchActive] = useState(false);
  const [rightPosition, setRightPosition] = useState("30px");
  const [notifyConTitle, setNotifyConTitle] = useState("");
  const [notifyConDesc, setNotifyConDesc] = useState("");
  const waitingPopUp = useSelector((state: any) => state.game.waitingPopup);

  useClickOutside(componentRefProfileArea, () => {
    setProfileActive(false);
  });

  useClickOutside(componentRefNotifyArea, () => {
    setNotifyActive(false);
  });

  const countries: Country[] = [
    { name: "Australia", code: "AU" },
    { name: "Brazil", code: "BR" },
    { name: "China", code: "CN" },
    { name: "Egypt", code: "EG" },
    { name: "France", code: "FR" },
    { name: "Germany", code: "DE" },
    { name: "India", code: "IN" },
    { name: "Japan", code: "JP" },
    { name: "Spain", code: "ES" },
    { name: "United States", code: "US" },
  ];

  useEffect(() => {
    if (searchText === "") {
      setSearchedUsers([]);
      setCounterToFetch(0);
      return;
    }
    if (counterToFetch > 0) {
      setTimeout(() => {
        setCounterToFetch((state) => state - 1);
      }, 1000);
    } else {
      const ret = dispatch(userThunks.searchForUsers(searchText) as any);
      if (ret instanceof Promise) {
        ret.then((res: any) => {
          if (res instanceof AxiosError) {
            setSearchedUsers([]);
          } else {
            setSearchedUsers(res.data.body);
          }
        });
      }
    }
  }, [counterToFetch, dispatch, searchText]);

  const [notifyActive, setNotifyActive] = useState(false);
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

  useEffect(() => {
    if (waitingPopUp) {
      setNotifyActive(false);
      setProfileActive(false);
    }
  }, [waitingPopUp]);

  const getNormalNotifications = async () => {
    await dispatch(notifyThunks.getNormalNotifications() as any);
  };

  const getFriendNotifications = async () => {
    await dispatch(notifyThunks.getFriendNotifications() as any);
  };

  const openWaitingPopUp = () => {
    dispatch(gameThunks.changeWaitingPopup(true) as any);
  };

  return (
    <div className={classes.header_container}>
      <NotificationsContainer
        isActive={notifyActive}
        setNotifyActive={setNotifyActive}
        setNotifyCnt={setNotifyCnt}
        notifications={notifications}
        rightPosition={rightPosition}
        title={notifyConTitle}
        desc={notifyConDesc}
      />
      <div
        className={`${classes.profile_area} ${
          profileActive ? classes.true : classes.false
        }`}
        ref={componentRefProfileArea}
      >
        <div className={classes.pa_info}>
          <ProfileImage
            exec={() => {
              setProfileActive((state) => !state);
              setNotifyActive(false);
            }}
            photoUrl={loggedUser.photoUrl}
            username={loggedUser.username}
            style={{
              cursor: "pointer",
              backgroundColor: loggedUser.accountColor,
              width: "40px",
              height: "40px",
              fontSize: "18px",
              marginRight: "5px",
              marginLeft: "5px",
            }}
            canClick={true}
          />
          <span>{loggedUser.username}</span>
        </div>
        <Link to={`/profile/${loggedUser.username}`}>
          <div className={classes.pa_menu}>
            <img src="/Assets/SVG/ProfileIconBlack.svg" alt="ProfilePicture" />
            <span>Profile</span>
          </div>
        </Link>
        <Link to="/profile/edit">
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
      <div className={classes.header_left_area}>
        {/* <i className={`bi bi-list ${classes.burger}`}></i> */}
        <BurgerMenu />
        <div className={classes.header_logo}>
          <Link to="/">
            <img src="/Assets/Images/GroofyLogoCover.png" alt="Logo" />
          </Link>
        </div>
        <div
          className={
            classes.search + " " + (searchActive ? classes.active : "")
          }
          onClick={(e) => op.current?.toggle(e)}
        >
          <i className="pi pi-search" />
          <span className={classes.search_text}>Search</span>
        </div>
        <OverlayPanel
          ref={op}
          showCloseIcon
          closeOnEscape
          dismissable={true}
          className={styles.search_overlay}
          onShow={() => {
            setSearchActive(true);
          }}
          onHide={() => {
            setSearchText("");
            setCounterToFetch(0);
            setSearchedUsers([]);
            setSearchActive(false);
          }}
        >
          <div className={styles.search_container}>
            <h1>Find a player</h1>
            <div className={`p-inputgroup flex-1 ` + styles.inputfield_div}>
              <InputText
                placeholder="Keyword"
                value={searchText}
                onChange={(e) => {
                  setSearchText(e.target.value);
                  setCounterToFetch(2);
                  setSearchedUsers([]);
                }}
                className={styles.inputfield}
              />
              {searchText === "" ? (
                <Button icon="pi pi-search" className={styles.searchBtn} />
              ) : (
                <Button
                  icon="pi pi-times-circle
                  "
                  className={styles.searchBtn}
                  onClick={() => {
                    setSearchText("");
                    setCounterToFetch(0);
                    setSearchedUsers([]);
                  }}
                />
              )}
            </div>
            {counterToFetch > 0 ? (
              <div className={styles.spinner}>
                <ProgressSpinner style={{ width: "40px" }} />
              </div>
            ) : (
              <>
                <div className={styles.players_div}>
                  {searchedUsers.map((user) => (
                    <Link to={`/profile/${user.username}`}>
                      <div className={styles.search_player}>
                        {user.photoUrl !== null ? (
                          <img src={user.photoUrl} alt="profile_photo" />
                        ) : (
                          <ProfileImage
                            photoUrl={user.photoUrl}
                            username={user.username}
                            style={{
                              backgroundColor: user.accountColor,
                              width: "40px",
                              height: "40px",
                              fontSize: "18px",
                              marginRight: "10px",
                            }}
                            canClick={false}
                          />
                        )}
                        <span>{user.username}</span>
                        {user.country && user.country !== "" && (
                          <ReactCountryFlag
                            countryCode={
                              countries.find(
                                (country) => country.name === user.country
                              )?.code || " "
                            }
                            svg
                            style={{
                              width: "1em",
                              height: "1em",
                              marginLeft: "8px",
                            }}
                            title={user.country || ""}
                          />
                        )}
                      </div>
                    </Link>
                  ))}
                </div>
                <div className={styles.footer}>
                  {searchedUsers.length === 0 ? (
                    <span>No results found</span>
                  ) : (
                    <span>
                      The most matched results for <span>{searchText}</span>
                    </span>
                  )}
                </div>
              </>
            )}
          </div>
        </OverlayPanel>
      </div>
      <div className={classes.header_user_area}>
        {loggedUser.existingGameId && (
          <div className={classes.ongoing_match}>
            <span>Game in progress</span>
            <Link to={`/game/${loggedUser.existingGameId}`}>View game</Link>
          </div>
        )}
        {loggedUser.existingInvitationId &&
          waitingPopUp !== null &&
          waitingPopUp !== true && (
            <div className={classes.pending_match}>
              <span>Pending game...</span>
              <button
                className={classes.view_pending_btn}
                onClick={openWaitingPopUp}
              >
                View
              </button>
            </div>
          )}
        <div className={classes.header_h_imgbox}>
          <ActionButton
            count={friendNotifyCnt}
            img="/Assets/SVG/people.svg"
            clickEvent={() => {
              setNotifyActive((prev) => !prev);
              setProfileActive(false);
              getFriendNotifications();
              setRightPosition("80px");
              setNotifyConTitle("Friend Requests");
              setNotifyConDesc("There are no friend requests");
            }}
          />
          <ActionButton
            count={messageNotifyCnt}
            img="/Assets/SVG/message.svg"
            clickEvent={() => {
              // setNotifyActive((prev) => !prev);
              // setProfileActive(false);
              // setMessageNewCnt(0);
              // setRightPosition("60px");
              // setNotifyConTitle("Messages");
              // setNotifyConDesc("There are no messages");
              navigate("/user/message");
            }}
          />
          <ActionButton
            count={normalNotifyCnt}
            img="/Assets/SVG/notificationsIcon.svg"
            clickEvent={() => {
              setNotifyActive((prev) => !prev);
              setProfileActive(false);
              getNormalNotifications();
              setRightPosition("30px");
              setNotifyConTitle("Notifications");
              setNotifyConDesc("There are no notifications");
            }}
          />
          <ProfileImage
            exec={() => {
              setProfileActive((state) => !state);
              setNotifyActive(false);
            }}
            photoUrl={loggedUser.photoUrl}
            username={loggedUser.username}
            style={{
              cursor: "pointer",
              backgroundColor: loggedUser.accountColor,
              width: "45px",
              height: "45px",
              fontSize: "20px",
            }}
            canClick={true}
          />
        </div>
      </div>
    </div>
  );
};

export default GroofyHeader;
