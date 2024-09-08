import React from "react";
import { Button } from "primereact/button";
import { classNames } from "primereact/utils";
import classes from "./scss/privacy.module.css";

const Privacy = () => {
  return (
    <div className={classes.edit_info}>
      <h3 className={classes.edit_header}>Privacy</h3>
      <form className={classes.edit_content}>
        <Button className={classNames(classes.groofybtn)} label="Save" />
      </form>
    </div>
  );
};

export default Privacy;
