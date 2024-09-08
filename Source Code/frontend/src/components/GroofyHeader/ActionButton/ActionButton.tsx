import { ActionButtonProps } from "../../../shared/types";
import classes from "./scss/actionbutton.module.css";

const ActionButton = (props: ActionButtonProps) => {
  return (
    <div className={classes.action} onClick={props.clickEvent}>
      {props.count > 0 && (
        <div className={classes.action_cnt}>
          <span>{props.count}</span>
        </div>
      )}
      <img className={classes.header_pr_action} src={props.img} alt="action" />
    </div>
  );
};

export default ActionButton;
