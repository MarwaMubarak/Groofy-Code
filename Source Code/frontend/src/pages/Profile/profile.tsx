import { SideBar, GroofyHeader, GBtn, SinglePost } from "../../components";
import "./scss/profile.css";
import { ChangeEvent } from "react";
import ReactCountryFlag from "react-country-flag";

const Profile = () => {
  const handleExpanding = (e: ChangeEvent<HTMLTextAreaElement>) => {
    autoExpand(e.target);
  };

  const autoExpand = (textarea: HTMLTextAreaElement) => {
    textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
  };
  return (
    <div className="newprofile-container">
      <SideBar idx={1} />
      <div className="userprofile align">
        <GroofyHeader />
        <div className="userprofile-side">
          <div className="up-side-left">
            <div className="up-info">
              <div className="up-info-img">
                <img src="/Assets/Images/Hazem Adel.jpg" />
              </div>
              <div className="up-info-details">
                <div className="up-info-d-box">
                  <h3>Rokba</h3>
                  <div className="up-info-d-box-edit">
                    <img src="/Assets/SVG/edit.svg" />
                    <span>Edit</span>
                  </div>
                </div>
                <h4>
                  Hazem Adel, Giza, Egypt
                  <ReactCountryFlag
                    countryCode="EG"
                    svg
                    style={{
                      width: "1em",
                      height: "1em",
                      marginLeft: "8px",
                    }}
                    title="Egypt"
                  />
                </h4>
                <p>
                  Dedicated competitive programmer excelling in algorithmic
                  mastery and problem-solving. Consistently achieving top ranks
                  in coding competitions. Driven by a passion for code
                  optimization and continuous improvement.
                </p>
                <div className="up-info-details-controls">
                  <GBtn
                    btnText="Add Friend"
                    icnSrc="/Assets/SVG/addfriend.svg"
                    clickEvent={() => {}}
                  />
                  <GBtn
                    btnText="Message"
                    icnSrc="/Assets/SVG/message.svg"
                    clickEvent={() => {}}
                  />
                </div>
              </div>
            </div>
            <div className="media-section">
              <form className="posts-container">
                <div className="post-header">
                  <div className="post-header-single active">
                    <h3>Posts</h3>
                  </div>
                  <div className="post-header-single">
                    <h3>My Friends</h3>
                  </div>
                  <div className="post-header-single">
                    <h3>Clan</h3>
                  </div>
                  <div className="post-header-single">
                    <h3>History</h3>
                  </div>
                </div>
                <div className="post-box">
                  <div className="post-row">
                    <img src="/Assets/Images/Hazem Adel.jpg" alt="" />
                    <textarea
                      placeholder="Share your coding insights and experiences"
                      onChange={handleExpanding}
                      maxLength={500}
                    ></textarea>
                    <GBtn
                      btnText="Quick Post"
                      icnSrc="/Assets/SVG/quick.svg"
                      clickEvent={() => {}}
                    />
                  </div>
                  <div className="posts">
                    <SinglePost
                      postUser="Hazem Adel"
                      postUserImg="/Assets/Images/Hazem Adel.jpg"
                      postContent="Hello world! this is my first post"
                    />
                    <SinglePost
                      postUser="Hazem Adel"
                      postUserImg="/Assets/Images/Hazem Adel.jpg"
                      postContent="I wanna to beat someone now!!"
                    />
                    <SinglePost
                      postUser="Hazem Adel"
                      postUserImg="/Assets/Images/Hazem Adel.jpg"
                      postContent="Ez"
                    />
                    <SinglePost
                      postUser="Hazem Adel"
                      postUserImg="/Assets/Images/Hazem Adel.jpg"
                      postContent="Hakona Matata XD."
                    />
                  </div>
                </div>
              </form>

              {/* <ProfileCard
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
          /> */}
            </div>
            {/* <div className="up-achievement"></div>
            <div className="up-history"></div> */}
          </div>
          <div className="up-side-right">
            <div className="profile-section">
              <div className="ps-info">
                <div className="ps-header">
                  <h3>Info</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className="info-btn"
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className="ps-container-box">
                  <div className="psi-single-details">
                    <span>
                      World Rank: <span className="beside"> #4529</span>
                    </span>
                  </div>
                  <div className="psi-single-details">
                    <span>
                      Last Seen: <span className="ls">Online</span>
                    </span>
                  </div>
                  <div className="psi-single-details">
                    <span>
                      Friends: <span className="friends">42</span>
                    </span>
                  </div>
                </div>
              </div>
              <div className="ps-info">
                <div className="ps-header">
                  <h3>Statistics</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className="info-btn"
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className="ps-container-box">
                  <div className="psi-single-details">
                    <span>
                      <img src="/Assets/Images/battleicon.png" />
                      Total Matches
                    </span>
                    <span className="any">800</span>
                  </div>
                  <div className="psi-single-details">
                    <span>
                      <img src="/Assets/Images/Yellow_trophy.png" />
                      Highest Trophies
                    </span>
                    <span className="any">5030</span>
                  </div>
                  <div className="psi-single-details">
                    <span>Wins</span>
                    <span className="any">733</span>
                  </div>
                  <div className="psi-single-details">
                    <span>Loses</span>
                    <span className="any">52</span>
                  </div>
                  <div className="psi-single-details">
                    <span>Draws</span>
                    <span className="any">15</span>
                  </div>
                </div>
              </div>
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
    </div>
  );
};

export default Profile;
