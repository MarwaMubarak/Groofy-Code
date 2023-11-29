import { useState } from "react";
import { Link } from "react-router-dom";
import "./scss/sidebar.css";

const SideBar = (probs: { idx: number }) => {
  const [sbActive, setSBActive] = useState(true);
  return (
    <div className={`sidebar-container ${sbActive}`}>
      <div className="sidebar-up">
        <div className="sidebar-header">
          <img src="/Assets/SVG/menu.svg" alt="Menu" />
          <span className="sidebar-logo">Menu</span>
        </div>
        <ul className="sidebar-nav-items">
          <Link to="/">
            <li className={`${!probs.idx ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  !probs.idx ? "HomeIconColored" : "HomeIcon"
                }.svg`}
                alt=""
              />
              <span>Home</span>
            </li>
          </Link>
          <Link to="/profile">
            <li className={`${probs.idx === 1 ? "active" : ""}`}>
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
            <li className={`${probs.idx === 2 ? "active" : ""}`}>
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
            <li className={`${probs.idx === 3 ? "active" : ""}`}>
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
            <li className={`${probs.idx === 4 ? "active" : ""}`}>
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
            <li className={`${probs.idx === 5 ? "active" : ""}`}>
              <img
                src={`/Assets/SVG/${
                  probs.idx === 5 ? "HelpIconColored" : "HelpIcon"
                }.svg`}
                alt=""
              />
              <span>Help</span>
            </li>
          </Link>
        </ul>
      </div>
      <div
        className="sidebar-down"
        onClick={() => setSBActive((state) => !state)}
      >
        <img src="/Assets/SVG/collapse.svg" alt="Collapse" />
        <span>Collapse</span>
      </div>
    </div>
  );
};

export default SideBar;
