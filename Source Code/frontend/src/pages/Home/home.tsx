import {
  GBtn,
  GroofyHeader,
  Friends,
  Blog,
  ProfileCard,
  EventCard,
  FollowCard
} from "../../components";
import "./scss/home.css";

const Home = () => {
  return (
    <>
      <GroofyHeader idx={0} />
      <Friends />
      <div className="home-container">
        <div className="play-container">
          <div className="gamemode">
            <div className="gm-info">
              <h3>
                Casual<span>Match</span>
              </h3>
            </div>
            <div className="gm-details">
              <p>
                Empower players to create their perfect match by allowing them
                to customize every aspect.
              </p>
              <GBtn btnText="Battle" clickEvent={() => {}} />
            </div>
          </div>
          <div className="gamemode">
            <div className="gm-info">
              <h3>
                Ranked<span>Match</span>
              </h3>
            </div>
            <div className="gm-details">
              <p>
                Challenge your skills and climb the ranks with the option to
                play in a competitive ranked match.
              </p>
              <GBtn btnText="Battle" clickEvent={() => {}} />
            </div>
          </div>
        </div>
        <div className="activity-section align">
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
             <FollowCard/>
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;
