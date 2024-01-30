import { btnProps } from "../../shared/types";
import classes from "./scss/gbtn.module.css";

const GBtn = (props: btnProps) => {
  return (
    <button
      className={classes.groofybtn}
      disabled={props.btnState}
      onClick={props.clickEvent}
      type={`${props.btnType ? "submit" : "button"}`}
    >
      {props.icnSrc && (
        <img className={classes.btn_icn} src={`${props.icnSrc}`} alt="Run" />
      )}
      <span className={classes.btn_text}>{props.btnText}</span>
    </button>
  );
};

export default GBtn;
