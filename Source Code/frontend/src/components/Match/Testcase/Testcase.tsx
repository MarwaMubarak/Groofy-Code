import { TestcaseProps } from "../../../shared/types";
import classes from "./scss/testcase.module.css";

const Testcase = (props: TestcaseProps) => {
  return (
    <div className={classes.testcase}>
      <div className={classes.card}>
        <span className={classes.tc_title}>Input</span>
        <pre className={classes.tc_text}>
          {props.input}
          <img
            className={classes.copy_icn}
            src="/Assets/SVG/copy.svg"
            alt="Copy"
          />
        </pre>
      </div>
      <div className={classes.card}>
        <span className={classes.tc_title}>Output</span>
        <pre className={classes.tc_text}>
          {props.output}
          <img
            className={classes.copy_icn}
            src="/Assets/SVG/copy.svg"
            alt="Copy"
          />
        </pre>
      </div>
    </div>
  );
};

export default Testcase;
