import "./scss/groofyheader.scss";

const GroofyHeader = () => {
  return (
    <div className="header-container"> 
      <div className="header-logo">
        <img src="/Assets/Images/GroofyCodeLogo.png" alt="Logo" />
      </div>
      <div className="header-user-area">
        <span className="header-h-usn">Mahmoud</span>
        <div className="header-h-imgbox">
          <img
            className="header-pr-ph"
            src="/Assets/Images/profile_picture.jpg"
            alt="ProfilePhoto"
          />
        </div>
        <button className="exit-btn">
          <img src="/Assets/SVG/exit.svg" alt="Exit" />
          {/* <span>Logout</span> */}
        </button>
      </div>
    </div>
  );
};

export default GroofyHeader;
