import { Link } from "react-router-dom";
import "./scss/singlefriend.css";
import UserPopUp from "../../UserPopUP/UserPopUp";
import { SingleFriendProps } from "../../../shared/types";

const SingleFriend = (props: SingleFriendProps) => {
  return (
    <div className="singlefriend-container">
      <UserPopUp
        username="tourist"
        userImg="/Assets/Images/tourist.jpg"
        status="In Game"
        gameType="Ranked Match"
        clanImg="/Assets/Images/clan1.png"
        clanName="Ghosts"
        rankImg="/Assets/Images/elite-rank.png"
        rankName="Elite"
        badges={[
          ["Groofy Predator", "/Assets/Images/apex-predator-rank.png"],
          ["High Accuracy", "/Assets/Images/attackbadge.png"],
          ["Master Wins", "/Assets/Images/win20badge.png"],
        ]}
      />
      <div className="sf-info">
        <div className="sf-img">
          <Link to="/profile/1">
            <img src={props.userImg} alt="FriendPhoto" />
          </Link>
        </div>
        <div className="sf-details">
          <h3>{props.username}</h3>
          <p>{props.status}</p>
        </div>
      </div>
      <div className="sf-invite">
        <img src="/Assets/SVG/profile-invite.svg" alt="InviteFriend" />
      </div>
    </div>
  );
};

export default SingleFriend;
