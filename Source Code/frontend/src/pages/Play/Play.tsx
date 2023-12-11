import { Gamemode, GroofyHeader, SideBar } from "../../components";
import "./scss/play.css";

const Play = () => {
  return (
    <div className="play-page">
      <SideBar idx={2} />
      <div className="play-div align">
        <GroofyHeader />
        <div className="play-mid-menu">
          <div className="play-left-menu">
            <div className="play-img-user">
              <img src="/Assets/Images/profile_picture.jpg" />
            </div>
            <div className="play-versus-img">
              <img src="/Assets/Images/versus.png" />
            </div>
            <div className="play-img-user">
              <img src="/Assets/Images/Hazem Adel.jpg" />
            </div>
          </div>
          <div className="play-menu">
            <h3 className="play-title">Play a match</h3>
            <div className="play-modes">
              <Gamemode
                title="Velocity Code"
                img="/Assets/Images/clock.png"
                description="Play a 15-min match and be a fast coder."
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Casual Match"
                img="/Assets/Images/battle.png"
                description="Challenge others in a friendly competetion"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Ranked Match"
                img="/Assets/Images/ranked.png"
                description="Climb up the ranks to be the master coder"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Custom Match"
                img="/Assets/Images/customize.png"
                description="Play or create matches that suit your interests"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Beat a Friend"
                img="/Assets/Images/friends.png"
                description="Compete against your friends to find the better coder"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="2 Vs 2"
                img="/Assets/Images/coop.png"
                description="Team up with your friend against others"
                type="full"
                clickEvent={() => {}}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Play;
