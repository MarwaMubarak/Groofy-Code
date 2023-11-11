import SingleFriend from "./SingleFriend/SingleFriend";
import "./scss/friends.css";

const Friends = () => {
  return (
    <div className="friends-container">
        <div className="friends-header">
            <div className="fh-img">
                <img
                    src="/Assets/Images/profile_picture.jpg"
                    alt="ProfilePhoto"
                />
                <h3>Friends</h3>
            </div>
            <img className="fh-arrow"
                src="/Assets/SVG/arrow-down.svg"
                alt="ExpandArrow"
            />
        </div>
        <div className="friends-content">
            <div className="fc-search">
                <img
                    src="/Assets/SVG/search-icon.svg"
                    alt="SearchIcon"
                />
                <input type="search" placeholder="Search Friend" />
            </div>
            <div className="fc-online">

            </div>
            <div className="fc-friends">
                <SingleFriend/>
                <SingleFriend/>
            </div>
        </div>
       
    </div>
  );
};

export default Friends;
