import classes from "./scss/testcase.module.css";

const Testcase = () => {
  return (
    <div className={classes.testcase}>
      <div className={classes.card}>
        <span className={classes.tc_title}>Input</span>
        <pre className={classes.tc_text}>
          {"3 2\n1 2 1\n1 2\n1 3"}
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
          {"3\n6"}
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
