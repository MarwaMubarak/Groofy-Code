import classes from "./scss/match-header.module.css";
const MatchHeader = () => {
  return (
    <div className={classes.header_div}>
      <div className={classes.logo}>
        <span>
          Groofy<span>Code</span>
        </span>
      </div>
      <div className={classes.m_dur}>
        <span>19:36</span> left
      </div>
      <div className={classes.user_area}>
        <span className={classes.h_usn}>Username</span>
        <div className={classes.h_imgbox}>
          <img
            className={classes.pr_ph}
            src="/Assets/Images/defAv.png"
            alt="ProfilePhoto"
          />
        </div>
      </div>
    </div>
  );
};

export default MatchHeader;
