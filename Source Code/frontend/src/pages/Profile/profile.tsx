import {
  MainProfile,
  NavBar,
} from "../../components";
import "./scss/profile.css";

const Profile = () => {
  return (
    <>
      <NavBar idx={1} />
      <div className="mainprofile">
        <MainProfile/>
      </div>
    </>
  );
};

export default Profile;
