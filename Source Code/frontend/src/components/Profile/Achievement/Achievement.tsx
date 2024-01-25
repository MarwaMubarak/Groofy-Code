import "./scss/achievements.css";
import { achievementProps } from "../../../shared/types";

const ProfileAchievemnts = (props : achievementProps) => {
  return (
    <div className="achievement-container">
        <h3>{props.achievementTitle}</h3>
        <h4>{props.achievementName}</h4>
        <div className="achievement-picture-contanier">
            <img src={props.achievementImg}/> 
        </div>
        <div className="profile-trophies">
            <img src={props.achievementTrophy}/>
            <h3 style={{color: props.scoreColor}}>{props.achievementScore}</h3>
        </div>
    </div>
  );
};

export default ProfileAchievemnts;
