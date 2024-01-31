import React from "react";
import classes from "./scss/appearance.module.css";
import GBtn from "../../GBtn/GBtn";
import { classNames } from "primereact/utils";
import { Button } from "primereact/button";

const Appearance = () => {
  return (
    <div className={classes.edit_info}>
      <h3 className={classes.edit_header}>Appearance & Style</h3>
      <form className={classes.edit_content}>
        <Button className={classNames(classes.groofybtn)} label="Save" />
      </form>
    </div>
  );
};

export default Appearance;
