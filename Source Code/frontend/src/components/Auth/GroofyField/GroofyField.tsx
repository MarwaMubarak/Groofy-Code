import { GroofyInputProps } from "../../../shared/types";
import "./scss/groofyfield.css";

const GroofyField = (props: GroofyInputProps) => {
  return (
    <div className="groofy-input">
      <span className="gi-text">{props.giText}</span>
      <input
        className={`${props.errState ? "err" : ""}`}
        type={props.giType}
        placeholder={props.giPlaceholder}
        value={props.giValue}
        onChange={props.onChange}
        onBlur={props.onBlur}
      />
      {props.errState && <span className="err-msg">{props.errMsg}</span>}
    </div>
  );
};

export default GroofyField;
