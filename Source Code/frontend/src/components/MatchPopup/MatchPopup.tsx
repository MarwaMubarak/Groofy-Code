import { Link } from "react-router-dom";
import classes from "./scss/match-popup.module.css";
import { MatchPopupProps } from "../../shared/types";
import { useDispatch, useSelector } from "react-redux";
import { popupThunks } from "../../store/actions";

import useWindowSize from "react-use/lib/useWindowSize";
import Confetti from "react-confetti";
import { useEffect } from "react";
import { userThunks } from "../../store/actions";

const MatchPopup = (props: MatchPopupProps) => {
  const dispatch = useDispatch();
  const closePopUp = () => {
    dispatch(popupThunks.setPopUpState(false, {}) as any);
  };
  const { width, height } = useWindowSize();

  return (
    <div className={classes.match_overlay}>
      {props.submissions.length > 0 &&
        props.submissions.at(0).verdict === "Accepted" && (
          <Confetti width={width} height={height} />
        )}
      <div className={classes.match_popup}>
        <div className={classes.trophy_icn}>
          {props.submissions.length > 0 &&
          props.submissions.at(0).verdict === "Accepted" ? (
            <img src="/Assets/SVG/trophyIconYellow.svg" alt="Trophy" />
          ) : (
            <img src="/Assets/SVG/sad.svg" alt="Trophy" />
          )}
        </div>
        <h2 className={classes.match_status}>
          {props.submissions.length > 0 &&
          props.submissions.at(0).verdict === "Accepted"
            ? "Congratulations"
            : "Better Luck Next Time"}
        </h2>
        <div className={classes.info_game}>
          <div className={classes.info_game_attemps}>
            <span>{props.submissions.length}</span>
            <h3>Attemps</h3>
          </div>
          <div className={classes.info_game_elapsedtime}>
            {props.submissions.length > 0 &&
            props.submissions.at(0).verdict === "Accepted" ? (
              <>
                <span>{props.submissions.at(0).submissionTime}</span>
              </>
            ) : (
              <span>60:00</span>
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
