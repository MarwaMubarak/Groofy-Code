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
