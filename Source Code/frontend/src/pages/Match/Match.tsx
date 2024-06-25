import { useDispatch, useSelector } from "react-redux";
import {
  CodingSection,
  MatchHeader,
  ProblemSection,
  Scoreboard,
} from "../../components";
import classes from "./scss/match.module.css";
import { useEffect } from "react";
import { gameThunks } from "../../store/actions";

const Match = () => {
  const dispatch = useDispatch();
  const isLoading = useSelector((state: any) => state.match.isLoading);
  const status = useSelector((state: any) => state.match.status);
  const loggedUser = useSelector((state: any) => state.auth.user);
  const message = useSelector((state: any) => state.match.message);
  const match = useSelector((state: any) => state.match.match);

  useEffect(() => {
    const getCurrentMatch = async () => {
      await dispatch(
        gameThunks.getCurrentGame(loggedUser.existingGameId) as any
      );
    };

    if (loggedUser.existingGameId != null) {
      getCurrentMatch();
    }
  }, [dispatch, loggedUser.existingGameId]);

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
