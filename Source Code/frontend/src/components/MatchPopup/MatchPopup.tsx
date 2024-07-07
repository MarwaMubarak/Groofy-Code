import { Link } from "react-router-dom";
import classes from "./scss/match-popup.module.css";
import { MatchPopupProps } from "../../shared/types";
import { useDispatch, useSelector } from "react-redux";
import { gameThunks, popupThunks } from "../../store/actions";

import useWindowSize from "react-use/lib/useWindowSize";
import Confetti from "react-confetti";
import { useEffect, useState } from "react";
import { userThunks } from "../../store/actions";

const MatchPopup = (props: MatchPopupProps) => {
  const dispatch = useDispatch();
  const [date, setDate] = useState("");
  const gameStatus = useSelector((state: any) => state.game.gameStatus);
  const gameID = useSelector((state: any) => state.game.gameID);
  const closePopUp = () => {
    dispatch(popupThunks.setPopUpState(false, {}) as any);
    dispatch(userThunks.getProfile() as any);
  };
  const { width, height } = useWindowSize();
  const gameStartTime = useSelector((state: any) => state.game.startTime);

  useEffect(() => {
    const updateGame = async () => {
      await dispatch(gameThunks.getCurrentGame(gameID) as any);
    };
    updateGame();

    const now = Date.now();
    const timeLeft = Math.max(now - new Date(gameStartTime).getTime(), 0);

    const minutes = Math.floor(timeLeft / (1000 * 60));
    const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
    setDate(`${minutes}:${seconds < 10 ? "0" : ""}${seconds}`);
  }, []);

  const getMatchHeader = () => {
    if (gameStatus === "FINISHED") {
      if (
        props.submissions.length > 0 &&
        props.submissions.at(0).verdict === "Accepted"
      ) {
        return "Congratulations";
      } else {
        return "Better Luck Next Time";
      }
    } else if (gameStatus === "OPPONENT_LEFT") {
      return "Your Opponent has Left";
    } else {
      return "Game Over";
    }
  };

  const getGamePhoto = () => {
    if (gameStatus === "FINISHED") {
      if (
        props.submissions.length > 0 &&
        props.submissions.at(0).verdict === "Accepted"
      ) {
        return "/Assets/SVG/trophyIconYellow.svg";
      } else {
        return "/Assets/SVG/sad.svg";
      }
    } else if (gameStatus === "OPPONENT_LEFT") {
      return "/Assets/SVG/surrender.svg";
    } else {
      return "/Assets/SVG/sad.svg";
    }
  };
  return (
    <div className={classes.match_overlay}>
      {props.submissions.length > 0 &&
        props.submissions.at(0).verdict === "Accepted" && (
          <Confetti width={width} height={height} />
        )}
      <div className={classes.match_popup}>
        <div className={classes.trophy_icn}>
          <img src={getGamePhoto()} alt="Trophy" />
        </div>
        <h2 className={classes.match_status}>{getMatchHeader()}</h2>
        <div className={classes.info_game}>
          <div className={classes.info_game_attemps}>
            <span>{props.submissions.length}</span>
            <h3>Attemps</h3>
          </div>
          <div className={classes.info_game_elapsedtime}>
            {props.submissions.length > 0 &&
            props.submissions.at(0).verdict === "Accepted" ? (
              <span>{props.submissions.at(0).submissionTime}</span>
            ) : (
              <span>{date}</span>
            )}
            <h3>Elapsed Time</h3>
          </div>
        </div>
        {props.matchType === "Ranked" && (
          <div className={classes.result_box}>
            <h3>Rating Changes: </h3>
            {/* <img src="/Assets/SVG/trophyIconYellow.svg" alt="Trophy" /> */}
            <div className={classes.result}>
              <span>{props.oldRank}</span>
              <i className="bi bi-arrow-right"></i>
              <h5
                className={`${
                  props.oldRank < props.newRank
                    ? classes.win
                    : props.oldRank > props.newRank
                    ? classes.lose
                    : classes.draw
                }`}
              >
                {props.newRank}
              </h5>
            </div>
          </div>
        )}
        <div className={classes.btn_box}>
          <Link to="/" className={classes.match_btn} onClick={closePopUp}>
            Back to Home
          </Link>
        </div>
      </div>
    </div>
  );
};

export default MatchPopup;
