import { GBtn, GroofyHeader, GroofyFooter } from "../../components";
import "./scss/new_home.css";

const New_Home = () => {
  return (
    <>
      <GroofyHeader idx={0} />
      <div className="home-container">
        <div className="play-container">
          <div className="gamemode">
            <div className="gm-info">
              <h3>Casual</h3>
              <p>
                Empower players to create their perfect match by allowing them
                to customize every aspect.
              </p>
            </div>
            <GBtn btnText="Battle" clickEvent={() => {}} />
          </div>
          <div className="gamemode">
            <div className="gm-info">
              <h3>Ranked</h3>
              <p>
                Challenge your skills and climb the ranks with the option to
                play in a competitive ranked match.
              </p>
            </div>
            <GBtn btnText="Battle" clickEvent={() => {}} />
          </div>
        </div>
      </div>
    </>
  );
};

export default New_Home;
