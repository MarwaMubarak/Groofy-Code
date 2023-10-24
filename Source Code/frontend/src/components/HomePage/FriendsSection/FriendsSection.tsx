import "./scss/friendssection.css"


const FriendsSection = () => {
  return(
    <div className="friends-background">
        <div className="friends-header">
          <img src="./Assets/friends.png" alt="Friends"/>
          <h2>Friends</h2>
        </div>
        <ul className="friends-body">
          <li>
            <img src="./Assets/defAv2.png" alt="Friend1"/>
            <h3>
              Hazem Adel
            </h3>
          </li>
          <li>
            <img src="./Assets/defAv.png" alt="Friend1"/>
            <h3>
              Mahmoud Abdelrady
            </h3>
          </li>
        </ul>
    </div>
  );
  };
  
  export default FriendsSection;
  