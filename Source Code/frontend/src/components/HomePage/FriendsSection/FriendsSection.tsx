import "./scss/friendssection.css";

const FriendsSection = () => {
  return (
    <div className="friends-background">
      <div className="friends-header">
        <img src="./Assets/SVG/friends.svg" alt="Friends" />
        <h2>Friends</h2>
      </div>
      <ul className="friends-body">
        <li>
          <img src="./Assets/Images/defAv2.png" alt="Friend" />
          <h3>Hazem Adel</h3>
        </li>
        <li>
          <img src="./Assets/Images/defAv.png" alt="Friend" />
          <h3>Mahmoud Abdelrady</h3>
        </li>
      </ul>
    </div>
  );
};

export default FriendsSection;
