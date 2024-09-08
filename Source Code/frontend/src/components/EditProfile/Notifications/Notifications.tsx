import React from "react";
import classes from "./scss/notifications.module.css";
import { Button } from "primereact/button";
import { classNames } from "primereact/utils";

const Notifications = () => {
  return (
    <div className={classes.edit_info}>
      <h3 className={classes.edit_header}>Notifications</h3>
      <form className={classes.edit_content}>
        <Button className={classNames(classes.groofybtn)} label="Save" />
      </form>
    </div>
  );
};

export default Notifications;
