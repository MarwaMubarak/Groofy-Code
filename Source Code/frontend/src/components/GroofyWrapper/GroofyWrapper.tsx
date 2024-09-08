import { ReactNode } from "react";
import SideBar from "../SideBar/SideBar";
import classes from "./scss/groofywrapper.module.css";

const GroofyWrapper = ({
  children,
  idx,
}: {
  children: ReactNode;
  idx: number;
}) => {
  return (
    <div className={classes.groofy_wrapper}>
      <SideBar idx={idx} />
      <div className={classes.groofy_container}>
        {/* <GroofyHeader /> */}
        {children}
      </div>
    </div>
  );
};

export default GroofyWrapper;
