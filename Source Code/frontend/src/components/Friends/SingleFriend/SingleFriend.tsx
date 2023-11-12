import "./scss/singlefriend.css";

const SingleFriend = () => {
  return (
    <div className="singlefriend-container">
      <div className="sf-popup">
        <div className="sfp-section1">
          <h3>Rokba</h3>
          <div className="sfp-section1-box">
            {/* <img className="sfp-section1-skin"
              src="/Assets/Images/TagSkin.png"
              alt="Skin"
            /> */}
            <img className="sfp-section1-pic"
              src="/Assets/Images/Hazem Adel.jpg"
              alt="ProfilePicture"
            />
          </div>
        </div>
        <div className="sfp-section2">
            <h3>In Game</h3>
            <span>Ranked Match</span>
        </div>
        <div className="sfp-section3">
            <div className="sfp-section3-box">
              <img 
                src="/Assets/Images/elite-rank.png"
                alt="RankPicture"
              />
              <div className="sfp-section3-box-title">
                <span>Rank</span>
                <h3>Elite</h3>
              </div>
            </div>
            <div className="sfp-section3-box">
              <img 
                src="/Assets/Images/clan1.png"
                alt="ClanPicture"
              />
              <div className="sfp-section3-box-title">
                <span>Clan</span>
                <h3>Ghosts</h3>
              </div>
            </div>
        </div>
        <div className="sfp-section4">
          <span>Badges</span>
          <div className="spf-section4-box">
              <img 
                src="/Assets/Images/apex-predator-rank.png"
                alt="Badge1"
              />
              <img 
                src="/Assets/Images/attackbadge.png"
                alt="Badge2"
              />
              <img 
                src="/Assets/Images/win20badge.png"
                alt="Badge3"
              />
          </div>
        </div>
      </div>
      <div className="sf-info">
        <div className="sf-img">
          <img src="/Assets/Images/profile_picture.jpg" alt="FriendPhoto" />
        </div>
        <div className="sf-details">
          <h3>Hazem Adel</h3>
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
