import "./scss/groofyheader.css";
import { Link } from "react-router-dom";

const GroofyHeader = (probs: { idx: number }) => {
  return (
    <div className="header-container">
      <div className="header-logo">
        <span>
          Groofy<span>Code</span>
        </span>
      </div>
      <ul className="header-nav-items">
        <Link to="/">
          <li className={`${!probs.idx ? "active" : ""}`}>
            <img
              src={`/Assets/SVG/${
                !probs.idx ? "HomeIconColored" : "HomeIcon"
              }.svg`}
              alt=""
            />
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
          </li>
        </Link>
      </ul>
      <div className="header-user-area">
        <div className="header-h-imgbox">
          <img
            className="header-pr-ph"
            src="/Assets/Images/profile_picture.jpg"
            alt="ProfilePhoto"
          />
        </div>
      </div>
    </div>
  );
};

export default GroofyHeader;
