import "./scss/singlefollowcard.css";
import { useState } from "react";
import { Link } from "react-router-dom";
import { SingleFollowCardProps } from "../../../../shared/types";
const SingleFollowCard = (props: SingleFollowCardProps) => {
  const [followActive, setFollowActive] = useState(false);
  return (
    <div className="single-follow-card">
        <div className="sfcard-info">
            <Link to="/profile">
                <img src={props.userImg} alt="FriendPhoto" />
            </Link>
            <h3>{props.username}</h3>
        </div>
        {followActive == false ?
        (<span className="follow" onClick={() => {
          setFollowActive((state) => !state);}}>Follow</span>)
        :
        (
          <span className="unfollow" onClick={() => {setFollowActive((state) => !state);}}>Following</span>
        )
        }
    </div>
  );
};

export default SingleFollowCard;
