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

const Home = () => {
  return (
    <div className="home-container">
      <SideBar idx={0} />
      <div className="activity-section align">
        <GroofyHeader />
        <div className="play-section">
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
          <div className="gamemode-box">
            <Gamemode
              title="Casual"
              img="/Assets/Images/battle.png"
              clickEvent={() => {}}
            />
            <Gamemode
              title="Clan"
              img="/Assets/Images/clan.png"
              clickEvent={() => {}}
            />
            <Gamemode
              title="2 Vs 2"
              img="/Assets/Images/coop.png"
              clickEvent={() => {}}
            />
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
              <div className="extra">
                <img src="/Assets/SVG/extra.svg" alt="AddImage" />
                <span>Add More Details</span>
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
