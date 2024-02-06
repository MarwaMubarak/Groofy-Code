import { Chat, GroofyWrapper } from "../../components";
import classes from "./scss/clan.module.css";

const Clan = () => {
  return (
    <GroofyWrapper idx={3}>
      <div className={classes.clan_div}>
        <div className={classes.c_info}>
          <div className={classes.c_dashboard}>
            <div className={classes.c_details_wrapper}>
              <div className={classes.c_details}>
                <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
                <span>The Elites</span>
              </div>
              <div className={classes.c_actions}>
                <i className="bi bi-gear-fill" />
                <i className="bi bi-box-arrow-left" />
              </div>
            </div>
            <div className={classes.c_stats}>
              <div className={classes.c_stat}>
                <span className={classes.stat_title}>Members:</span>
                <span className={classes.stat_num}>4/10</span>
              </div>
              <div className={classes.c_stat}>
                <span className={classes.stat_title}>World Rank:</span>
                <span className={classes.stat_num}>#10</span>
              </div>
              <div className={classes.c_stat}>
                <span className={classes.stat_title}>Total Matches:</span>
                <span className={classes.stat_num}>12</span>
              </div>
              <div className={classes.c_stat}>
                <span className={classes.stat_title}>Wins:</span>
                <span className={classes.stat_num}>7</span>
              </div>
              <div className={classes.c_stat}>
                <span className={classes.stat_title}>Losses:</span>
                <span className={classes.stat_num}>3</span>
              </div>
            </div>
          </div>
          <div className={classes.c_box}>
            <div className={classes.c_header}>
              <h3 className={classes.c_title}>Members</h3>
              <img
                className={classes.ch_icn}
                src="/Assets/SVG/view-all.svg"
                alt="ViewAll"
              />
            </div>
            <div className={classes.m_box}>
              <div className={classes.member}>
                <div className={classes.m_info}>
                  <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
                  <span className={classes.m_usn}>Username</span>
                </div>
                <div className={classes.m_online}>Online</div>
              </div>
              <div className={classes.member}>
                <div className={classes.m_info}>
                  <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
                  <span className={classes.m_usn}>Username</span>
                </div>
                <div className={classes.m_online}>Online</div>
              </div>
            </div>
          </div>
        </div>
        <Chat type="clan" />
      </div>
    </GroofyWrapper>
  );
};

export default Clan;
