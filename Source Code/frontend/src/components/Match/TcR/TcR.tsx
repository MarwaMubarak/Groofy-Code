import { tcrProps } from "../../../shared/types";
import "./scss/tcr.css";

const TcR = (props: tcrProps) => {
  return (
    <div className="tcr">
      <img className="tcr-icn" src={props.icnSrc} alt="TcRStatus" />
      <span className="tcr-text">Testcase {props.tcNum}</span>
      {!props.tcStatus && (
        <span className="details" onClick={props.detailsEvent}>
          Details
        </span>
      )}
    </div>
  );
};

export default TcR;
