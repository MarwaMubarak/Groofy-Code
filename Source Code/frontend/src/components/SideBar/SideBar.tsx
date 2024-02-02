import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { OverlayPanel } from "primereact/overlaypanel";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { ProgressSpinner } from "primereact/progressspinner";
import { userThunks } from "../../store/actions";
import { AxiosError } from "axios";
import ReactCountryFlag from "react-country-flag";
import classes from "./scss/sidebar.module.css";
import styles from "./scss/overlay.module.css";

interface Country {
  name: string;
  code: string;
}
const SideBar = (probs: { idx: number }) => {
  const dispatch = useDispatch();
  const lsb =
    localStorage.getItem("sbActive") === ("true" || null) ? true : false;
  const [sbActive, setSBActive] = useState<boolean>(lsb);
  const user = useSelector((state: any) => state.auth.user);
  const op = useRef<OverlayPanel>(null);
  const [searchText, setSearchText] = useState<string>("");
  const [counterToFetch, setCounterToFetch] = useState<number>(2);
  const [searchedUsers, setSearchedUsers] = useState<any[]>([]);
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
    if (counterToFetch > 0) {
      setTimeout(() => {
        setCounterToFetch((state) => state - 1);
        console.log("TIMe");
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
  return (
    <div
      className={`${classes.sidebar_container} ${!sbActive && classes.false}`}
    >
      <div className={classes.sidebar_up}>
        <div className={classes.sidebar_header}>
          <img src="/Assets/SVG/codeIcon2.svg" alt="Menu" />
          <span className={classes.sidebar_logo}>Menu</span>
        </div>
        <ul className={classes.sidebar_nav_items}>
          <Link to="/">
            <li className={`${!probs.idx && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  !probs.idx ? "HomeIconColored" : "HomeIcon"
                }.svg`}
                alt=""
              />
              <span>Home</span>
            </li>
          </Link>
          <Link to={`/profile/${user.username}`}>
            <li className={`${probs.idx === 1 && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 1 ? "ProfileIconColored" : "ProfileIcon"
                }.svg`}
                alt=""
              />
              <span>Profile</span>
            </li>
          </Link>
          <Link to="/play">
            <li className={`${probs.idx === 2 && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 2 ? "BattleIconColored" : "BattleIcon"
                }.svg`}
                alt=""
              />
              <span>Play</span>
            </li>
          </Link>
          <Link to="/clan">
            <li className={`${probs.idx === 3 && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 3 ? "ClanIconColored" : "ClanIcon"
                }.svg`}
                alt=""
              />
              <span>Clan</span>
            </li>
          </Link>
          <Link to="/news">
            <li className={`${probs.idx === 4 && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 4 ? "NewsIconColored" : "NewsIcon"
                }.svg`}
                alt=""
              />
              <span>News</span>
            </li>
          </Link>
          <Link to="/help">
            <li className={`${probs.idx === 5 && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 5 ? "HelpIconColored" : "HelpIcon"
                }.svg`}
                alt=""
              />
              <span>Help</span>
            </li>
          </Link>
          <div
            className={classes.search}
            onClick={(e) => op.current?.toggle(e)}
          >
            <li className={`${probs.idx === 6 && classes.active}`}>
              <i className="pi pi-search" />
              <span>Search</span>
            </li>
          </div>
          <OverlayPanel
            ref={op}
            showCloseIcon
            closeOnEscape
            dismissable={true}
            className={styles.search_overlay}
            onHide={() => {
              setSearchText("");
              setCounterToFetch(0);
              setSearchedUsers([]);
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
        </ul>
      </div>
      <div
        className={classes.sidebar_down}
        onClick={() => {
          setSBActive((state: boolean) => !state);
          localStorage.setItem("sbActive", (!sbActive).toString());
        }}
      >
        <img src="/Assets/SVG/collapse.svg" alt="Collapse" />
        <span>Collapse</span>
      </div>
    </div>
  );
};

export default SideBar;
