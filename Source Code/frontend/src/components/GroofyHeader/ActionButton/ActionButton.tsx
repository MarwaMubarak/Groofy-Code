import { useState } from "react";
import { Link } from "react-router-dom";
import "./scss/actionbutton.css";
import { ActionButtonProps } from "../../../shared/types";

const ActionButton = (props: ActionButtonProps) => {
  return (
    <div className="action" onClick={props.clickEvent}>
      {props.count > 0 && (
        <div className="action-cnt">
          <span>{props.count}</span>
        </div>
      )}
      <img className="header-pr-action" src={props.img} alt="action" />
    </div>
  );
};

export default ActionButton;
