import { useState } from "react";
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import classes from "./scss/sidebar.module.css";

const SideBar = (probs: { idx: number }) => {
  const lsb =
    localStorage.getItem("sbActive") === ("true" || null) ? true : false;
  const [sbActive, setSBActive] = useState<boolean>(lsb);
  const user = useSelector((state: any) => state.auth.user);
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
          <Link to="/teams">
            <li className={`${probs.idx === 4 && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 4 ? "team-colored" : "team"
                }.svg`}
                alt=""
              />
              <span>Teams</span>
            </li>
          </Link>
          <Link to="/leaderboard">
            <li className={`${probs.idx === 5 && classes.active}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 5 ? "leaderboard_colored" : "leaderboard"
                }.svg`}
                alt=""
              />
              <span>Leaderboard</span>
            </li>
          </Link>
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
