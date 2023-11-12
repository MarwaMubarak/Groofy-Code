import { useState } from "react";
import SingleFriend from "./SingleFriend/SingleFriend";
import "./scss/friends.css";
const Friends = () => {
  const [friendsActive, setFriendsActive] = useState(false);
  return (
    <div className={`friends-container ${friendsActive}`}>
      <div
        className="friends-header"
        onClick={() => setFriendsActive((prev) => !prev)}
      >
        <div className="fh-img">
          <img src="/Assets/Images/profile_picture.jpg" alt="ProfilePhoto" />
          <h3>Friends</h3>
        </div>
        <img
          className={`fh-arrow ${friendsActive}`}
          src="/Assets/SVG/arrow-down.svg"
          alt="ExpandArrow"
        />
      </div>
      <div className="friends-content">
        <div className="fc-search">
          <img src="/Assets/SVG/search-icon.svg" alt="SearchIcon" />
          <input type="text" placeholder="Search Friends" />
        </div>
        <div className="fc-online">
          <span className="fc-box total active">Contacts (20)</span>
          <span className="fc-box online">Requests (5)</span>
        </div>
        <div className="fc-friends">
          <SingleFriend />
          {/* <SingleFriend /> */}
          {/* <SingleFriend /> */}
          {/* <SingleFriend /> */}
          {/* <SingleFriend /> */}
        </div>
      </div>
    </div>
  );
};

export default Friends;
