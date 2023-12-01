import { Link } from "react-router-dom";
import { ProfileCardProps } from "../../../shared/types";
import "./scss/profilecard.css";

const ProfileCard = (props: ProfileCardProps) => {
  return (
    <div className="profile-card">
      {/* <div className="pc-section1">
        <div className="pc-skin"></div>
        <div className="pc-section1-box">
          <Link to="/profile">
            <img
              className="pc-section1-pic"
              src={props.userImg}
              alt="ProfilePicture"
            />
          </Link>
        </div>
        <h3>{props.username}</h3>
        <p>{props.bio}</p>
      </div>
      <div className="pc-section2">
        <span className="pc-header">16500 / 25000 XP</span>
        <div className="progress">
          <span className="progress-level">15</span>
          <div className="progress-box" style={{ width: "70%" }}></div>
        </div>
        <div className="pc-stats">
          <span className="pcs-box">
            <span className="pcsb-total">#219</span>
            <span className="pcsb-title">Rank</span>
          </span>
          <span className="pcs-box">
            <span className="pcsb-total">1217</span>
            <span className="pcsb-title">Followers</span>
          </span>
        </div>
      </div> */}
      <div className="pc-section3">
        <div className="pc-section3-box">
          <img src={props.rankImg} alt="RankPicture" />
          <div className="pc-section3-box-title">
            <span>Rank</span>
            <h3>{props.rankName}</h3>
          </div>
        </div>
        <div className="pc-section3-box">
          <img src={props.clanImg} alt="ClanPicture" />
          <div className="pc-section3-box-title">
            <span>Clan</span>
            <h3>{props.clanName}</h3>
          </div>
        </div>
      </div>
      <div className="pc-section4">
        <span>Badges</span>
        <div className="pc-section4-box">
          {props.badges.map(([badgeName, badgeImg]) => (
            <div className="pc-section4-box-badge">
              <img src={badgeImg} alt="badge" />
              <h3>{badgeName}</h3>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProfileCard;
