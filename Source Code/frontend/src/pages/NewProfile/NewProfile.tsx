import { GroofyFooter, GroofyHeader, NavBar } from "../../components";
import "./scss/newprofile.css";

const NewProfile = () => {
  return (
    <div className="profile-div align">
      <GroofyHeader />
      <div className="p-wrapper">
        <NavBar idx={1} />
        <div className="p-content">
          <div className="p-info">
            <div className="p-bio">
              <div className="bio-box">
                <div className="exp"></div>
                <div className="bio">
                  <div className="bb">
                    <h3>Username</h3>
                    <p>Real Name, City, Country</p>
                  </div>
                  <div className="bb">
                    <span className="bb-info">
                      World Rank: <span className="wr">#9720</span>
                    </span>
                    <span className="bb-info">
                      Last Seen: <span className="ls">Online</span>
                    </span>
                    <span className="bb-info">
                      Friend of: <span className="fr">512 solvers</span>
                    </span>
                    <span className="bb-info">
                      Member since: <span className="ms">08/11/2023</span>
                    </span>
                  </div>
                </div>
              </div>
              <div className="rec-wrapper">
                <div className="rec-box">
                  <div className="rc-i">
                    <h3>Rank</h3>
                    <p>Name of Rank</p>
                  </div>
                  <div className="rec-circle"></div>
                  <div className="rec-cups rank">
                    <img src="/Assets/SVG/trophyIconYellow.svg" alt="trophy" />
                    2586
                  </div>
                </div>
                <div className="rec-box">
                  <div className="rc-i">
                    <h3>Clan</h3>
                    <p>Name of Clan</p>
                  </div>
                  <div className="rec-circle"></div>
                  <div className="rec-cups clan">
                    <img src="/Assets/SVG/trophyIconVio.svg" alt="trophy" />
                    2586
                  </div>
                </div>
              </div>
            </div>
            <div className="stats">
              <div className="st-row">
                <div className="st-item">
                  <span className="sti-text">Total Matches</span>
                  <span className="sti-num">800</span>
                </div>
                <div className="st-item">
                  <div className="sti-text">Highest Trophies</div>
                  <span className="sti-num">5030</span>
                </div>
              </div>
              <div className="st-row">
                <div className="st-item">
                  <span className="sti-text">Wins</span>
                  <div className="sti-num">733</div>
                </div>
                <div className="st-item">
                  <span className="sti-text">Loses</span>
                  <div className="sti-num">52</div>
                </div>
              </div>
              <div className="st-row">
                <div className="st-item">
                  <span className="sti-text">Draws</span>
                  <div className="sti-num">15</div>
                </div>
                <div className="st-item">
                  <span className="sti-text">K/D/A</span>
                  <div className="sti-num">14</div>
                </div>
              </div>
            </div>
          </div>
          <div className="p-pic">
            <div className="pic-holder">
              <div className="pic-skin"></div>
            </div>
            <div className="badges-holder">
              <img src="/Assets/SVG/edit.svg" alt="Edit" />
              <div className="badge"></div>
              <div className="badge"></div>
              <div className="badge"></div>
            </div>
          </div>
        </div>
      </div>
      <GroofyFooter />
    </div>
  );
};

export default NewProfile;
