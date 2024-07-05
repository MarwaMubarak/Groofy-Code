import { useDispatch, useSelector } from "react-redux";
import {
  CodingSection,
  MatchHeader,
  MatchPopup,
  ProblemSection,
  Scoreboard,
} from "../../components";
import classes from "./scss/match.module.css";
import { useEffect, useState } from "react";
import { gameThunks } from "../../store/actions";
import { createPortal } from "react-dom";
import { useNavigate } from "react-router-dom";

const Match = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const status = useSelector((state: any) => state.match.status);
  const loggedUser = useSelector((state: any) => state.auth.user);
  const message = useSelector((state: any) => state.match.message);
  const popupShow = useSelector((state: any) => state.popup.show);
  const popupBody = useSelector((state: any) => state.popup.body);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const tim = setTimeout(() => {
      setIsLoading(false);
    }, 1200);
    return () => {
      clearTimeout(tim);
    };
  }, []);

  useEffect(() => {
    const getCurrentMatch = async () => {
      await dispatch(
        gameThunks.getCurrentGame(loggedUser.existingGameId) as any
      );
    };

    if (loggedUser.existingGameId != null) {
      getCurrentMatch();
    } else {
      if (!popupShow) {
        navigate("/");
      }
    }
  }, [dispatch, loggedUser.existingGameId, navigate, popupShow]);

  if (isLoading) return <div>Loading...</div>;

  return (
    <>
      <MatchHeader />
      <div className={classes.match_div}>
        <Scoreboard />
        <div className={classes.match_sections}>
          <ProblemSection />
          <CodingSection />
          {popupShow &&
            createPortal(
              <MatchPopup
                matchResult={popupBody.gameResult}
                matchType={popupBody.gameType}
                newRank={popupBody.newRank}
                oldRank={loggedUser.user_rating}
                submissions={popupBody.submissionDTOS}
              />,
              document.getElementById("root") as any
            )}
        </div>
      </div>
    </>
  );
};

export default Match;
