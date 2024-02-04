import {
  CodingSection,
  MatchHeader,
  ProblemSection,
  Scoreboard,
} from "../../components";
import classes from "./scss/match.module.css";

const Match = () => {
  return (
    <>
      <MatchHeader />
      <div className={classes.match_div}>
        <Scoreboard />
        <div className={classes.match_sections}>
          <ProblemSection />
          <CodingSection />
        </div>
      </div>
    </>
  );
};

export default Match;
