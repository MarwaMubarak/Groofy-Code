import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import NotifyBox from "./NotifyBox/NotifyBox";
import ActionButton from "./ActionButton/ActionButton";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { authThunks, userThunks } from "../../store/actions";
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

interface Country {
  name: string;
  code: string;
}

const GroofyHeader = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state: any) => state.auth.user);
  const op = useRef<OverlayPanel>(null);
  const [searchText, setSearchText] = useState<string>("");
  const [counterToFetch, setCounterToFetch] = useState<number>(2);
  const [searchedUsers, setSearchedUsers] = useState<any[]>([]);
  const componentRefProfileArea = useRef(null);
  const componentRefNotifyArea = useRef(null);
  const [searchActive, setSearchActive] = useState(false);

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
          console.log(res);
          if (res instanceof AxiosError) {
            console.log(res.response?.data?.message);
            setSearchedUsers([]);
          } else {
            console.log(res.data.message);
            setSearchedUsers(res.data.body);
          }
        });
      }
    }
  }, [counterToFetch, dispatch, searchText]);
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
        ref={componentRefNotifyArea}
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
        ref={componentRefProfileArea}
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
                        <img src={user.photo.url} alt="ProfilePicture" />
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
