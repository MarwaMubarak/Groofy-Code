import "./scss/groofyheader.css";
import { Link } from "react-router-dom";

const GroofyHeader = (probs: { idx: number }) => {
  return (
    <div className="header-container">
      <div className="header-logo">
        <span>Groofy<span>Code</span></span>
      </div>
      <ul className="header-nav-items">
          <li className={`${!probs.idx ? "active" : ""}`}>
            <Link to="/">
            <svg width="800px" height="800px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <g id="SVGRepo_bgCarrier" stroke-width="0"/>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"/>
              <g id="SVGRepo_iconCarrier"> <path d="M22 22L2 22" stroke="#ffffff" stroke-width="0.8399999999999999" stroke-linecap="round"/> <path d="M2 11L6.06296 7.74968M22 11L13.8741 4.49931C12.7784 3.62279 11.2216 3.62279 10.1259 4.49931L9.34398 5.12486" stroke="#ffffff" stroke-width="0.8399999999999999" stroke-linecap="round"/> <path d="M15.5 5.5V3.5C15.5 3.22386 15.7239 3 16 3H18.5C18.7761 3 19 3.22386 19 3.5V8.5" stroke="#ffffff" stroke-width="0.8399999999999999" stroke-linecap="round"/> <path d="M4 22V9.5" stroke="#ffffff" stroke-width="0.8399999999999999" stroke-linecap="round"/> <path d="M20 9.5V13.5M20 22V17.5" stroke="#ffffff" stroke-width="0.8399999999999999" stroke-linecap="round"/> <path d="M15 22V17C15 15.5858 15 14.8787 14.5607 14.4393C14.1213 14 13.4142 14 12 14C10.5858 14 9.87868 14 9.43934 14.4393M9 22V17" stroke="#ffffff" stroke-width="0.8399999999999999" stroke-linecap="round" stroke-linejoin="round"/> <path d="M14 9.5C14 10.6046 13.1046 11.5 12 11.5C10.8954 11.5 10 10.6046 10 9.5C10 8.39543 10.8954 7.5 12 7.5C13.1046 7.5 14 8.39543 14 9.5Z" stroke="#ffffff" stroke-width="0.8399999999999999"/> </g>
            </svg>
            </Link>
          </li>
          <li className={`${probs.idx === 1 ? "active" : ""}`}>
            <Link to="/profile">
              <svg width="800px" height="800px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <g id="SVGRepo_bgCarrier" stroke-width="0"/>
                <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"/>

                <g id="SVGRepo_iconCarrier"> <g clip-path="url(#clip0_429_11217)"> <path d="M4 18C4 15.7908 5.79086 14 8 14H16C18.2091 14 20 15.7908 20 18V18C20 19.1045 19.1046 20 18 20H6C4.89543 20 4 19.1045 4 18V18Z" stroke="#ffffff" stroke-width="0.8399999999999999" stroke-linejoin="round"/> <circle cx="12" cy="6.99997" r="3" stroke="#ffffff" stroke-width="0.8399999999999999"/> </g> <defs> <clipPath id="clip0_429_11217"> <rect width="24" height="24" fill="white"/> </clipPath> </defs> </g>

              </svg>
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
