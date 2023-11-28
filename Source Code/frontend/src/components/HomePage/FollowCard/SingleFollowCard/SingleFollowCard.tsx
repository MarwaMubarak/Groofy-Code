import "./scss/singlefollowcard.css";
import { Link } from "react-router-dom";
import { SingleFollowCardProps } from "../../../../shared/types";
const SingleFollowCard = (props: SingleFollowCardProps) => {
  return (
    <div className="single-follow-card">
        <div className="sfcard-info">
            <Link to="/profile/1">
                <img src={props.userImg} alt="FriendPhoto" />
            </Link>
            <h3>{props.username}</h3>
        </div>
        <span>Follow</span>
    </div>
  );
};

export default SingleFollowCard;
