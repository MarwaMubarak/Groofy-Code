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
      {props.icnSrc && !props.btnState && (
        <img className={classes.btn_icn} src={`${props.icnSrc}`} alt="Run" />
      )}
      <span className={classes.btn_text}>
        {props.btnState && props.btnText === "" ? (
          <i className="pi pi-spin pi-spinner" style={{ fontSize: "24px" }}></i>
        ) : (
          props.btnText
        )}
      </span>
    </button>
  );
};

export default GBtn;
