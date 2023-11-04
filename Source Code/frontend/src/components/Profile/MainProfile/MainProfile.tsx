import GBtn from "../../GBtn/GBtn";
import InformationSection from "../InformationSection/InformationSection";
import ProfileAchievemnts from "../Achievement/Achievement"
import ProfilePicture from "../ProfilePicture/ProfilePicture";
import "./scss/mainprofile.css";

const MainProfile = () => {
  return (
      <div className="backgroundProfile">
          <div className="profile-settings-buttons">
              <div className="profile-nav-btn-container active">
              <GBtn btnText = {"Profile"} clickEvent={()=>{}}/>
              </div>
              <div className="profile-nav-btn-container">
              <GBtn btnText = {"Settings"} clickEvent={()=>{}}/>
              </div>
              <div className="profile-nav-btn-container">
              <GBtn btnText = {"My Friends"} clickEvent={()=>{}}/>
              </div>
              <div className="profile-nav-btn-container">
              <GBtn btnText = {"Clan"} clickEvent={()=>{}}/>
              </div>
              <div className="profile-nav-btn-container">
              <GBtn btnText = {"History"} clickEvent={()=>{}}/>
              </div>
          </div>
          <div className="profile-info">
              <InformationSection/>
              <ProfileAchievemnts
              achievementImg="/Assets/Images/elite-rank.png"
              achievementTitle="Rank"
              achievementName="Elite"
              achievementScore = {2586} 
              achievementTrophy="/Assets/Images/Yellow_trophy.png"
              scoreColor="#FD9B02"
              clickEvent={() => {}}/>
              <ProfileAchievemnts
              achievementImg="/Assets/Images/clan1.png"
              achievementTitle="Clan"
              achievementName="Ghost Riders"
              achievementScore = {2586} 
              achievementTrophy="/Assets/Images/Purple_trophy.png"
              scoreColor= "#7723BA"
              clickEvent={() => {}}/>
              <ProfilePicture profileImg= "" tagSkinImg= "/Assets/Images/TagSkin.png" clickEvent={() => {}}/>
          </div>
      </div>
  );
};

export default MainProfile;
