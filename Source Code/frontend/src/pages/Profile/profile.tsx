import {
  GroofyFooter,
  GroofyHeader,
  MainProfile,
  NavBar,
} from "../../components";
import "./scss/profile.css";

const Profile = () => {
  return (
    <div className="profile-page-style align">
      <GroofyHeader idx={1}/>
      <div className="profile-main">
        <NavBar idx={1} />
        <MainProfile />
      </div>
      <GroofyFooter />
    </div>
  );
};

export default Profile;
