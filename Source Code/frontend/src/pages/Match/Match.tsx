import { useDispatch, useSelector } from "react-redux";
import {
  CodingSection,
  MatchHeader,
  ProblemSection,
  Scoreboard,
} from "../../components";
import classes from "./scss/match.module.css";
import { useEffect } from "react";
import { matchThunks } from "../../store/actions";

const Match = () => {
  const dispatch = useDispatch();
  const isLoading = useSelector((state: any) => state.match.isLoading);
  const status = useSelector((state: any) => state.match.status);
  const message = useSelector((state: any) => state.match.message);
  const match = useSelector((state: any) => state.match.match);

  useEffect(() => {
    const getCurrentMatch = async () => {
      await dispatch(matchThunks.getCurrentMatch() as any);
    };

    getCurrentMatch();
  }, [dispatch]);

  return isLoading ? (
    <div>Loading...</div>
  ) : status === "failure" ? (
    <div>{message}</div>
  ) : (
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
