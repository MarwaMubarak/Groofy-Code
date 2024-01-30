import classes from "./scss/scoreboard.module.css";

const Scoreboard = () => {
  return (
    <div className={classes.scoreboard}>
      <div className={classes.m_infobox}>
        <div className={classes.dot}></div>
        <div className={classes.match_info}>Casual Match | 30 Min</div>
      </div>
      <div className={classes.players}>
        <div className={classes.player}>
          <div className={classes.first_status}></div>
          <div className={classes.info}>
            <div className={classes.usn}>Username</div>
            <div className={classes.img}>
              <img src="/Assets/Images/defAv.png" alt="ProfilePhoto" />
            </div>
          </div>
          <div className={classes.pr_st + " " + classes.first}>
            <div className={classes.pr + " " + classes.y}>1</div>
            <div className={classes.pr + " " + classes.n}>2</div>
            <div className={classes.pr + " " + classes.p}>3</div>
          </div>
        </div>
        <span>VS</span>
        <div className={classes.player}>
          <div className={classes.pr_st + " " + classes.second}>
            <div className={classes.pr + " " + classes.y}>1</div>
            <div className={classes.pr + " " + classes.n}>2</div>
            <div className={classes.pr + " " + classes.y}>3</div>
          </div>
          <div className={classes.info}>
            <div className={classes.usn}>Username</div>
            <div className={classes.img}>
              <img src="/Assets/Images/defAv2.png" alt="ProfilePhoto" />
            </div>
          </div>
          <div className={classes.second_status}>
            <div className={classes.finished}>
              <img src="/Assets/Images/success.png" alt="Success" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Scoreboard;
