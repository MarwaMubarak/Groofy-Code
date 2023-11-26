import { GBtn, GroofyHeader, Friends, Blog } from "../../components";
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
              <h1>status</h1>
          </div>
          <div className="blogs-container">
            {[1, 2, 3, 4, 5].map(() => (
            <Blog/>
            ))}
          </div>
          <div className="events-container">
              <h1>hgash</h1>
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;
