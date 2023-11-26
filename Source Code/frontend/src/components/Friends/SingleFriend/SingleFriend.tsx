import { Link } from "react-router-dom";
import "./scss/singlefriend.css";
import UserPopUp from "../../UserPopUP/UserPopUp";

const SingleFriend = () => {
  return (
    <div className="singlefriend-container">
      <UserPopUp/>
      <div className="sf-info">
        <div className="sf-img">
          <Link to="/profile/1">
            <img src="/Assets/Images/tourist.jpg" alt="FriendPhoto" />
          </Link>
        </div>
        <div className="sf-details">
          <h3>tourist</h3>
          <p>In Game</p>
        </div>
      </div>
      <div className="sf-invite">
        <img src="/Assets/SVG/profile-invite.svg" alt="InviteFriend" />
      </div>
    </div>
  );
};

export default SingleFriend;
