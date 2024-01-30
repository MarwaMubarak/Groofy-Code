import { ReactNode } from "react";
import classes from "./scss/groofywrapper.module.css";

const GroofyWrapper = ({ children }: { children: ReactNode }) => {
  return <div className={classes.align}>{children}</div>;
};

export default GroofyWrapper;
