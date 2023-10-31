import {
  CodingSection,
  MatchHeader,
  ProblemSection,
  Scoreboard,
} from "../../components";
import "./scss/match.css";

const Match = () => {
  return (
    <>
      <MatchHeader />
      <div className="match-div">
        <button className="exit-btn">
          <img src="/Assets/SVG/exit.svg" alt="Exit" />
          <span>Leave</span>
        </button>
        <Scoreboard />
        <div className="match-sections">
          <ProblemSection />
          <CodingSection />
        </div>
      </div>
    </>
  );
};

export default Match;
