import "./scss/scoreboard.css";
const Scoreboard = () => {
  return (
    <div className="scoreboard">
      <div className="m-infobox">
        <div className="dot"></div>
        <div className="match-info">Casual Match | 30 Min</div>
      </div>
      <div className="players">
        <div className="player">
          <div className="first-status"></div>
          <div className="info">
            <div className="usn">Username</div>
            <div className="img">
              <img src="/Assets/Images/defAv.png" alt="ProfilePhoto" />
            </div>
          </div>
          <div className="pr-st first">
            <div className="pr y">1</div>
            <div className="pr n">2</div>
            <div className="pr p">3</div>
          </div>
        </div>
        <span>VS</span>
        <div className="player">
          <div className="pr-st second">
            <div className="pr y">1</div>
            <div className="pr n">2</div>
            <div className="pr y">3</div>
          </div>
          <div className="info">
            <div className="usn">Username</div>
            <div className="img">
              <img src="/Assets/Images/defAv2.png" alt="ProfilePhoto" />
            </div>
          </div>
          <div className="second-status">
            <div className="finished">
              <img src="/Assets/Images/success.png" alt="Success" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Scoreboard;
