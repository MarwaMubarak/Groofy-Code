import "./scss/followcard.css";
import SingleFollowCard from "./SingleFollowCard/SingleFollowCard";
const FollowCard = () => {
  return (
    <div className="follow-card">
      <h3 className="fc-header">Suggested to follow</h3>
      <SingleFollowCard username="tourist" userImg= "/Assets/Images/tourist.jpg"/>
      <SingleFollowCard username="Mr.Robot" userImg= "/Assets/Images/authPic.png"/>
      <SingleFollowCard username="Killer" userImg= "/Assets/Images/defAv.png"/>
      <SingleFollowCard username="Naruto_Uzumaki" userImg= "/Assets/Images/defAv2.png"/>
    </div>
  );
};

export default FollowCard;
