import {
  GBtn,
  GroofyHeader,
  Friends,
  Blog,
  ProfileCard,
  EventCard,
  FollowCard,
  GroofyFooter,
  SideBar,
  Gamemode,
} from "../../components";
import "./scss/home.css";
import { useState } from "react";

const Home = () => {
  const [likeActive, setLikeActive] = useState(false);
  const [currReact, setCurrReact] = useState("like");
  const [reactions, setReactions] = useState(false);
  return (
    <div className="home-container">
      <SideBar idx={0} />
      <div className="activity-section align">
        <GroofyHeader />
        <div className="play-section">
          <div className="play-section-features">
            <h3 className="p-s-f-header">Play</h3>
            <div className="play-container">
              <Gamemode
                title="Velocity Code"
                description="Face off in a 10-minute coding duel. Strategize, code swiftly, and emerge victorious in this high-stakes test of programming prowess."
                img="/Assets/Images/clock.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Ranked Match"
                description="Climb the coding ranks in intense head-to-head battles. Prove your skills, rise to the top, and become the coding champion."
                img="/Assets/Images/ranked.png"
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
          <form className="posts-container">
            <h3 className="post-header">Posts</h3>
            <div className="post-box">
              <div className="post-row">
                <img src="/Assets/Images/Hazem Adel.jpg" alt="" />
                <input
                  type="text"
                  placeholder="Share your coding insights and experiences"
                />
                <GBtn
                  btnText="Quick Post"
                  icnSrc="/Assets/SVG/quick.svg"
                  clickEvent={() => {}}
                />
              </div>
              <div className="single-post">
                <div className="single-post-info">
                  <div className="single-post-info-div">
                    <img
                      className="s-p-img"
                      src="/Assets/Images/Hazem Adel.jpg"
                    ></img>
                    <div className="s-p-details">
                      <h3>Hazem Adel</h3>
                      <p>Hello world! this is my first post</p>
                    </div>
                  </div>
                  <div className="s-p-reactbtn">
                    <div
                      className="react-button like"
                      onClick={() => {
                        setLikeActive((state) => !state);
                        setCurrReact("like");
                      }}
                      onMouseEnter={() => setReactions(true)}
                      onMouseLeave={() => setReactions(false)}
                    >
                      {likeActive === false ? (
                        <>
                          <img
                            src="/Assets/SVG/Like white.svg"
                            alt="Reaction"
                          ></img>
                          <span>3.2k</span>
                        </>
                      ) : currReact === "like" ? (
                        <>
                          <img
                            src="/Assets/SVG/Like blue.svg"
                            alt="Reaction"
                          ></img>
                          <span>3.2k</span>
                        </>
                      ) : currReact === "love" ? (
                        <>
                          <img src="/Assets/SVG/Love Icon.svg" alt="Reaction" />
                          <span>3.2k</span>
                        </>
                      ) : currReact === "haha" ? (
                        <>
                          <img
                            src="/Assets/SVG/Laugh Icon.svg"
                            alt="Reaction"
                          />
                          <span>3.2k</span>
                        </>
                      ) : (
                        <>
                          <img
                            src="/Assets/SVG/Angry Icon.svg"
                            alt="Reaction"
                          />
                          <span>3.2k</span>
                        </>
                      )}
                    </div>
                    <div
                      className={`reactions-popup ${reactions}`}
                      onMouseEnter={() => setReactions(true)}
                      onMouseLeave={() => setReactions(false)}
                    >
                      <div className="r-p-box">
                        <div className="r-p-box-img">
                          <img
                            src="/Assets/SVG/reaction-like.svg"
                            alt="Reaction"
                            onClick={() => {
                              setLikeActive(true);
                              setCurrReact("like");
                              setReactions(false);
                            }}
                          />
                        </div>
                        <span>Like</span>
                      </div>
                      <div className="r-p-box">
                        <div className="r-p-box-img">
                          <img
                            src="/Assets/SVG/reaction-love.svg"
                            alt="Reaction"
                            onClick={() => {
                              setLikeActive(true);
                              setCurrReact("love");
                              setReactions(false);
                            }}
                          />
                        </div>
                        <span>Love</span>
                      </div>
                      <div className="r-p-box">
                        <div className="r-p-box-img">
                          <img
                            src="/Assets/SVG/reaction-laugh.svg"
                            alt="Reaction"
                            onClick={() => {
                              setLikeActive(true);
                              setCurrReact("haha");
                              setReactions(false);
                            }}
                          />
                        </div>
                        <span>Haha</span>
                      </div>
                      <div className="r-p-box">
                        <div className="r-p-box-img">
                          <img
                            src="/Assets/SVG/reaction-angry.svg"
                            alt="Reaction"
                            onClick={() => {
                              setLikeActive(true);
                              setCurrReact("angry");
                              setReactions(false);
                            }}
                          />
                        </div>
                        <span>Angry</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="s-p-reactions-cnt">
                  <img
                    className="single-reaction"
                    src="/Assets/SVG/reaction-like.svg"
                    alt="Reaction"
                  />
                  <img
                    className="single-reaction"
                    src="/Assets/SVG/reaction-love.svg"
                    alt="Reaction"
                  />
                  <img
                    className="single-reaction"
                    src="/Assets/SVG/reaction-laugh.svg"
                    alt="Reaction"
                  />
                  {/* <img
                    className="single-reaction"
                    src="/Assets/SVG/reaction-angry.svg"
                    alt="Reaction"
                  /> */}
                </div>
              </div>
            </div>
          </form>
          <ProfileCard
            username="Hazem Adel"
            bio="Student at FCAI - Cairo University | ECPC’23 Champion - Candidate Master @Codeforces"
            worldRank={5}
            followers={5}
            level={5}
            percentage={30}
            userImg="/Assets/Images/Hazem Adel.jpg"
            clanImg="/Assets/Images/clan1.png"
            clanName="Ghosts"
            rankImg="/Assets/Images/elite-rank.png"
            rankName="Elite"
            badges={[
              ["Groofy Predator", "/Assets/Images/apex-predator-rank.png"],
              ["High Accuracy", "/Assets/Images/attackbadge.png"],
              ["Master Wins", "/Assets/Images/win20badge.png"],
            ]}
          />
        </div>
        <div className="last-section">
          <div className="empty-section"></div>
          <FollowCard />
        </div>
      </div>
      {/* <div className="activity-section align">
          <div className="status-container">
            <ProfileCard
              username="Hazem Adel"
              bio="Student at FCAI - Cairo University | ECPC’23 Champion - Candidate Master @Codeforces"
              worldRank={5}
              followers={5}
              level={5}
              percentage={30}
              userImg="/Assets/Images/Hazem Adel.jpg"
              clanImg="/Assets/Images/clan1.png"
              clanName="Ghosts"
              rankImg="/Assets/Images/elite-rank.png"
              rankName="Elite"
              badges={[
                ["Groofy Predator", "/Assets/Images/apex-predator-rank.png"],
                ["High Accuracy", "/Assets/Images/attackbadge.png"],
                ["Master Wins", "/Assets/Images/win20badge.png"],
              ]}
            />
          </div>
          <div className="blogs-container">
            {[1, 2, 3, 4, 5].map(() => (
              <Blog />
            ))}
          </div>
          <div className="events-container">
             <EventCard title="Ranked Match" btn_title="Battle" details="Challenge your skills and climb the ranks with the option to
                play in a competitive ranked match." img="/Assets/Images/battle.png"/>
                <EventCard title="Casual Match" btn_title="Battle" details="Empower players to create their perfect match by allowing them to customize every aspect." img="/Assets/Images/battle.png"/>
             <FollowCard/>
          </div>
        </div> */}
    </div>
  );
};

export default Home;
