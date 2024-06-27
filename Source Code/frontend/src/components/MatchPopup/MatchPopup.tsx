import { Link } from "react-router-dom";
import classes from "./scss/match-popup.module.css";

const MatchPopup = () => {
  return (
    <div className={classes.match_overlay}>
      <div className={classes.match_popup}>
        <h2 className={classes.match_status}>Match Ended</h2>
        <h4 className={classes.match_result}>You won</h4>
        <div className={classes.result_box}>
          <img
            src="./Assets/SVG/trophyIconYellow.svg"
            alt="Trophy"
            className={classes.trophy_icn}
          />
          <div className={classes.result}>
            <span>1200</span>
            <i className="bi bi-arrow-right"></i>
            <h5 className={classes.win}>1500</h5>
          </div>
        </div>
        <table className={classes.match_stats}>
          <thead>
            <tr>
              <td>Time</td>
              <td>Verdict</td>
              <td>Language</td>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>03:00</td>
              <td>Accepted</td>
              <td>C++</td>
            </tr>
          </tbody>
        </table>
        <div className={classes.btn_box}>
          <Link to="/" className={classes.match_btn}>
            Back to Home
          </Link>
        </div>
      </div>
    </div>
  );
};

export default MatchPopup;
