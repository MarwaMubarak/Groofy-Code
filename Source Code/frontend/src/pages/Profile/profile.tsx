import {
  GroofyFooter,
  GroofyHeader,
  MainProfile,
  NavBar,
} from "../../components";
import "./scss/profile.css";

const Profile = () => {
  return (
    <div className="profile-page-style">
      <GroofyHeader idx={1}/>
      <div className="profile-main">
        <MainProfile />
      </div>
      <GroofyFooter />
    </div>
  );
};

export default Profile;
