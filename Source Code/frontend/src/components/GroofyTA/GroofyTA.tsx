import { ChangeEvent } from "react";
import classes from "./scss/groofyta.module.css";
import { GroofyTAProps } from "../../shared/types";

const GroofyTA = ({ taValue, changeHandler }: GroofyTAProps) => {
  const handleExpanding = (e: ChangeEvent<HTMLTextAreaElement>) => {
    autoExpand(e.target);
    changeHandler(e.target.value);
  };
  const autoExpand = (textarea: HTMLTextAreaElement) => {
    textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
  };
  return (
    <textarea
      className={classes.gta}
      value={taValue}
      placeholder="What's on your mind?"
      onChange={handleExpanding}
      maxLength={500}
    />
  );
};

export default GroofyTA;
