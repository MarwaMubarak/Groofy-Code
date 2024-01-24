import { Link } from "react-router-dom";
import "./scss/userpopup.css";
import { UserPopUpProps } from "../../shared/types";

const UserPopUp = (props: UserPopUpProps) => {
  return (
    <div className="sf-popup">
      <div className="sfp-section1">
        <h3>{props.username}</h3>
        <div className="sfp-section1-box">
          <Link to="/profile/1">
            <img
              className="sfp-section1-pic"
              src={props.userImg}
              alt="ProfilePicture"
            />
          </Link>
        </div>
      </div>
      <div className="sfp-section2">
        <h3>{props.status}</h3>
        <span>{props.gameType}</span>
      </div>
      <div className="sfp-section3">
        <div className="sfp-section3-box">
          <img src={props.rankImg} alt="RankPicture" />
          <div className="sfp-section3-box-title">
            <span>Rank</span>
            <h3>{props.rankName}</h3>
          </div>
        </div>
        <div className="sfp-section3-box">
          <img src={props.clanImg} alt="ClanPicture" />
          <div className="sfp-section3-box-title">
            <span>Clan</span>
            <h3>{props.clanName}</h3>
          </div>
        </div>
      </div>
      <div className="sfp-section4">
        <span>Badges</span>
        <div className="sfp-section4-box">
          {props.badges.map(([badgeName, badgeImg]) => (
            <div className="sfp-section4-box-badge">
              <img src={badgeImg} alt="badge" />
              <h3>{badgeName}</h3>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default UserPopUp;
