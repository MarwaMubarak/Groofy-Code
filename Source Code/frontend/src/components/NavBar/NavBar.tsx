import { Link } from "react-router-dom";
import "./scss/navbar.css";
const NavBar = (probs: { idx: number }) => {
  return (
    <div className="slider-style">
      <ul className="icons-style">
        <li className={`${!probs.idx ? "active" : ""}`}>
          <Link to="/">
            <img
              className="icon"
              src="/Assets/SVG/HomeIcon.svg"
              alt="NavBarHome"
            />
          </Link>
        </li>
        <li className={`${probs.idx === 1 ? "active" : ""}`}>
          <Link to="/profile">
            <img
              className="icon"
              src="/Assets/SVG/ProfileIcon.svg"
              alt="NavBarProfile"
            />
          </Link>
        </li>
        <li className={`${probs.idx === 2 ? "active" : ""}`}>
          <Link to="/play">
            <img
              className="icon"
              src="/Assets/SVG/BattleIcon.svg"
              alt="NavBarBattle"
            />
          </Link>
        </li>
        <li className={`${probs.idx === 3 ? "active" : ""}`}>
          <Link to="/clan">
            <img
              className="icon"
              src="/Assets/SVG/ClanIcon.svg"
              alt="NavBarClan"
            />
          </Link>
        </li>
        <li className={`${probs.idx === 4 ? "active" : ""}`}>
          <Link to="/news">
            <img
              className="icon"
              src="/Assets/SVG/NewsIcon.svg"
              alt="NavBarNews"
            />
          </Link>
        </li>
        <li className={`${probs.idx === 5 ? "active" : ""}`}>
          <Link to="/help">
            <img
              className="icon"
              src="/Assets/SVG/HelpIcon.svg"
              alt="NavBarHelp"
            />
          </Link>
        </li>
      </ul>
    </div>
  );
};

export default NavBar;
