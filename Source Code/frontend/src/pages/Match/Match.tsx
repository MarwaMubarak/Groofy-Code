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
        <button className={classes.exit_btn}>
          <img src="/Assets/SVG/exit.svg" alt="Exit" />
          <span>Leave</span>
        </button>
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
