import { profileImageProps } from "../../../shared/types";
import "./scss/profilepicture.css";

const ProfilePicture = (props: profileImageProps) => {
  return (
    <div className="profilepicture-container">
      <img className="profile-skin" src={props.tagSkinImg} alt="skin" />
      <div className="profile-image"></div>
      <img
        className="edit-button-style"
        src="/Assets/Images/editbutton.png"
        alt="edit"
      />
      <div className="badges-contanier">
        <img
          className="badges-contanier-img1"
          src="/Assets/Images/win20badge.png"
          alt="cont1"
        />
        <img
          className="badges-contanier-img2"
          src="/Assets/Images/highrankbadge.png"
          alt="cont2"
        />
        <img
          className="badges-contanier-img3"
          src="/Assets/Images/attackbadge.png"
          alt="cont3"
        />
      </div>
    </div>
  );
};

export default ProfilePicture;
