import "./scss/singlefriend.css";

const SingleFriend = () => {
  return (
    <div className="singlefriend-container">
        <div className="sf-info">
            <div className="sf-img">
                <span>Master</span>
                <img
                    src="/Assets/Images/profile_picture.jpg"
                    alt="FriendPhoto"
                />
            </div>
            <div className="sf-details">
                <h3>Hazem Adel</h3>
                <p>In Game</p>
            </div>
        </div>
        <div className="sf-invite">
            <img
                src="/Assets/SVG/profile-invite.svg"
                alt="InviteFriend"
            />
        </div>
    </div>
  );
};

export default SingleFriend;
