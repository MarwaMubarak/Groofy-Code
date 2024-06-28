import { Link } from "react-router-dom";
import classes from "./scss/match-popup.module.css";
import { MatchPopupProps } from "../../shared/types";
import { useDispatch } from "react-redux";
import { popupThunks } from "../../store/actions";

const MatchPopup = (props: MatchPopupProps) => {
  const dispatch = useDispatch();
  const closePopUp = () => {
    dispatch(popupThunks.setPopUpState(false, {}) as any);
  };

  return (
    <div className={classes.match_overlay}>
      <div className={classes.match_popup}>
        <h2 className={classes.match_status}>Match Ended</h2>
        <h4 className={classes.match_result}>{props.matchResult}</h4>
        {props.matchType === "Ranked" && (
          <div className={classes.result_box}>
            <img
              src="/Assets/SVG/trophyIconYellow.svg"
              alt="Trophy"
              className={classes.trophy_icn}
            />
            <div className={classes.result}>
              <span>{props.oldRank}</span>
              <i className="bi bi-arrow-right"></i>
              <h5 className={classes.win}>{props.newRank}</h5>
            </div>
          </div>
        )}
        <table className={classes.match_stats}>
          <thead>
            <tr>
              <td>Time</td>
              <td>Verdict</td>
              <td>Language</td>
            </tr>
          </thead>
          <tbody>
            {props.submissions.map((submission, idx) => (
              <tr key={idx}>
                <td>{submission.submissionTime}</td>
                <td>{submission.verdict}</td>
                <td>{submission.language}</td>
              </tr>
            ))}
          </tbody>
        </table>
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
