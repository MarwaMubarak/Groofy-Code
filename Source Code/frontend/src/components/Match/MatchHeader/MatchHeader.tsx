import "./scss/match-header.css";
const MatchHeader = () => {
  return (
    <div className="header-div">
      <div className="logo">
        <span>
          Groofy<span>Code</span>
        </span>
      </div>
      <div className="m-dur">
        <span>19:36</span> left
      </div>
      <div className="user-area">
        <span className="h-usn">Username</span>
        <div className="h-imgbox">
          <img
            className="pr-ph"
            src="/Assets/Images/defAv.png"
            alt="ProfilePhoto"
          />
        </div>
      </div>
    </div>
  );
};

export default MatchHeader;
