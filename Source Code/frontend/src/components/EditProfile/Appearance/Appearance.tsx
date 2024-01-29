import React from "react";
import classes from "./scss/appearance.module.css";
import GBtn from "../../GBtn/GBtn";

const Appearance = () => {
  return (
    <div className={classes.edit_info}>
      <h3 className={classes.edit_header}>Appearance & Style</h3>
      <div className={classes.edit_content}></div>
      <GBtn btnText="Save Changes" clickEvent={() => {}} />
    </div>
  );
};

export default Appearance;
