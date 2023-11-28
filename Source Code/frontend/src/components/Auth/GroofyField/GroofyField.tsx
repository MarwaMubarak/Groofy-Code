import { GroofyInputProps } from "../../../shared/types";
import "./scss/groofyfield.css";

const GroofyField = (props: GroofyInputProps) => {
  return (
    <div className="groofy-input">
      <span className="gi-text">{props.giText}</span>
      <input type={props.giType} placeholder={props.giPlaceholder} />
    </div>
  );
};

export default GroofyField;
