import { useRef } from "react";
import { Toast } from "primereact/toast";
import {
  GroofyHeader,
  SideBar,
  Gamemode,
  PostsContainer,
} from "../../components";
import "./scss/home.css";

const Home = () => {
  const toast = useRef<Toast>(null);
  return (
    <div className="home-container">
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
      <SideBar idx={0} />
      <div className="activity-section align">
        <GroofyHeader />
        <div className="play-section">
          <div className="play-section-features">
            <h3 className="p-s-f-header">Play</h3>
            <div className="play-container">
              <Gamemode
                title="Velocity Code"
                description="Face off in a 15-minute coding duel. Strategize, code swiftly, and emerge victorious in this high-stakes test of programming prowess."
                img="/Assets/Images/clock.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Ranked Match"
                description="Climb the coding ranks in intense head-to-head battles. Prove your skills, rise to the top, and become the coding champion."
                img="/Assets/Images/ranked.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Join Clan"
                description="Level up your gamplay and form alliances as you become a part of a gaming community by joining a clan."
                img="/Assets/Images/clan.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Solo Practice"
                description="Sharpen your skills and prepare for battle by practicing against a computer opponent."
                img="/Assets/Images/lightbulb.png"
                clickEvent={() => {}}
              />
            </div>
          </div>
          <div className="play-section-gamemode">
            <h3 className="p-s-g-header">Game Modes</h3>
            <div className="gamemode-box">
              <Gamemode
                title="Casual Match"
                img="/Assets/Images/battle.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Custom Match"
                img="/Assets/Images/customize.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="2 Vs 2"
                img="/Assets/Images/coop.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Beat a Friend"
                img="/Assets/Images/friends.png"
                clickEvent={() => {}}
              />
            </div>
          </div>
        </div>
        <div className="media-section">
          <div className="home-posts-container">
            <h3 className="hpc-title">Posts</h3>
            <PostsContainer toast={toast} />
          </div>
          <div className="profile-section">
            <div className="ps-info">
              <div className="ps-header">
                <h3>Division</h3>
                <abbr title="Info">
                  <img
                    src="/Assets/SVG/info.svg"
                    className="info-btn"
                    alt="Info"
                  />
                </abbr>
              </div>
              <div className="ps-container">
                <div className="psi-box">
                  <img src="/Assets/Images/elite-rank.png" alt="RankImg" />
                  <div className="wrapper">
                    <span>Rank</span>
                    <h3>Elite</h3>
                  </div>
                </div>
                <div className="psi-box">
                  <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
                  <div className="wrapper">
                    <span>Clan</span>
                    <h3>Ghosts</h3>
                  </div>
                </div>
              </div>
            </div>
            <div className="ps-info">
              <div className="ps-header">
                <h3>Badges</h3>
                <abbr title="Info">
                  <img
                    src="/Assets/SVG/info.svg"
                    className="info-btn"
                    alt="Info"
                  />
                </abbr>
              </div>
              <div className="ps-container">
                <div className="psi-badge">
                  <img
                    src="/Assets/Images/apex-predator-rank.png"
                    alt="Badge"
                  />
                  <span>Groofy Predator</span>
                </div>
                <div className="psi-badge">
                  <img src="/Assets/Images/attackbadge.png" alt="Badge" />
                  <span>High Accuracy</span>
                </div>
                <div className="psi-badge">
                  <img src="/Assets/Images/win20badge.png" alt="Badge" />
                  <span>Master Wins</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
