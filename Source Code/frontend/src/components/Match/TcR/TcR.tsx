import { tcrProps } from "../../../shared/types";
import classes from "./scss/tcr.module.css";

const TcR = (props: tcrProps) => {
  return (
    <div className={classes.tcr}>
      <img className={classes.tcr_icn} src={props.icnSrc} alt="TcRStatus" />
      <span className={classes.tcr_text}>Testcase {props.tcNum}</span>
      {!props.tcStatus && (
        <span className={classes.details} onClick={props.detailsEvent}>
          Details
        </span>
      )}
    </div>
  );
};

export default TcR;
