import classes from "./scss/problemsbar.module.css";

const ProblemsBar = () => {
  return (
    <div className={classes.problems_bar}>
      <span>Problems</span>
      <div className={classes.problems_container}>
        <div
          className={classes.problem + " " + classes.y + " " + classes.active}
        >
          1
        </div>
        <div className={classes.problem + " " + classes.n}>2</div>
        <div className={classes.problem + " " + classes.p}>3</div>
      </div>
    </div>
  );
};

export default ProblemsBar;
