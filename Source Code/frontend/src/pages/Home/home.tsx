import {
  GBtn,
  GroofyHeader,
  Friends,
  Blog,
  ProfileCard,
  EventCard,
  FollowCard,
  GroofyFooter
} from "../../components";
import "./scss/home.css";

const Home = () => {
  return (
    <>
      <GroofyHeader idx={0} />
      <Friends />
      <div className="home-container">
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
             <EventCard title="Ranked Match" btn_title="Battle" details="Challenge your skills and climb the ranks with the option to
                play in a competitive ranked match." img="/Assets/Images/battle.png"/>
                <EventCard title="Casual Match" btn_title="Battle" details="Empower players to create their perfect match by allowing them to customize every aspect." img="/Assets/Images/battle.png"/>
             <FollowCard/>
          </div>
        </div>
      </div>
      <GroofyFooter/>
    </>
  );
};

export default Home;
