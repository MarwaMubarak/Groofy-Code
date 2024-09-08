import { GroofyInputProps } from "../../../shared/types";
import classes from "./scss/groofyfield.module.css";

const GroofyField = (props: GroofyInputProps) => {
  return (
    <div className={classes.groofy_input}>
      <span className={classes.gi_text}>{props.giText}</span>
      <input
        className={`${props.errState && classes.err}`}
        type={props.giType}
        placeholder={props.giPlaceholder}
        value={props.giValue}
        onChange={props.onChange}
        onBlur={props.onBlur}
      />
      {props.errState && (
        <span className={classes.err_msg}>{props.errMsg}</span>
      )}
    </div>
  );
};

export default GroofyField;
