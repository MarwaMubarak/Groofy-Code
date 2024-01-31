import FormatDate from "../../../shared/functions/format-date";
import { PInfoProps } from "../../../shared/types";
import classes from "./scss/pinfo.module.css";

const PInfo = (props: PInfoProps) => {
  return (
    <div className={classes.up_side_right}>
      <div className={classes.profile_section}>
        <div className={classes.ps_info}>
          <div className={classes.ps_header}>
            <h3>Info</h3>
            <abbr title="Info">
              <img
                src="/Assets/SVG/info.svg"
                className={classes.info_btn}
                alt="Info"
              />
            </abbr>
          </div>
          <div className={classes.ps_container_box}>
            <div className={classes.psi_single_details}>
              <span>
                <img src="/Assets/SVG/calendar.svg" alt="calender" />
                Joined
                <span className={classes.beside}>
                  {FormatDate(props.profileUser.createdAt)}
                </span>
              </span>
            </div>
            <div className={classes.psi_single_details}>
              <span>
                World Rank: <span className={classes.beside}> {0}</span>
              </span>
            </div>
            <div className={classes.psi_single_details}>
              <span>
                Last Seen:
                <span className={classes.ls}>
                  {props.profileUser.isOnline ? "Online" : "Offline"}
                </span>
              </span>
            </div>
            <div className={classes.psi_single_details}>
              <span>
                Friends:
                <span className={classes.friends}>
                  {props.profileUser.friends?.length || 0}
                </span>
              </span>
            </div>
          </div>
        </div>
        <div className={classes.ps_info}>
          <div className={classes.ps_header}>
            <h3>Statistics</h3>
            <abbr title="Info">
              <img
                src="/Assets/SVG/info.svg"
                className={classes.info_btn}
                alt="Info"
              />
            </abbr>
          </div>
          <div className={classes.ps_container_box}>
            <div className={classes.psi_single_details}>
              <span>
                <img src="/Assets/Images/battleicon.png" alt="StatsIcon" />
                Total Matches
              </span>
              <span className={classes.any}>
                {props.profileUser.totalMatch}
              </span>
            </div>
            <div className={classes.psi_single_details}>
              <span>
                <img src="/Assets/Images/Yellow_trophy.png" alt="StatsIcon" />
                Highest Trophies
              </span>
              <span className={classes.any}>5030</span>
            </div>
            <div className={classes.psi_single_details}>
              <span>Wins</span>
              <span className={classes.any}>{props.profileUser.wins}</span>
            </div>
            <div className={classes.psi_single_details}>
              <span>Loses</span>
              <span className={classes.any}>{props.profileUser.loses}</span>
            </div>
            <div className={classes.psi_single_details}>
              <span>Draws</span>
              <span className={classes.any}>{props.profileUser.draws}</span>
            </div>
          </div>
        </div>
        <div className={classes.ps_info}>
          <div className={classes.ps_header}>
            <h3>Division</h3>
            <abbr title="Info">
              <img
                src="/Assets/SVG/info.svg"
                className={classes.info_btn}
                alt="Info"
              />
            </abbr>
          </div>
          <div className={classes.ps_container}>
            <div className={classes.psi_box}>
              <img src="/Assets/Images/elite-rank.png" alt="RankImg" />
              <div className={classes.wrapper}>
                <span>Rank</span>
                <h3>Elite</h3>
              </div>
            </div>
            <div className={classes.psi_box}>
              <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
              <div className={classes.wrapper}>
                <span>Clan</span>
                <h3>Ghosts</h3>
              </div>
            </div>
          </div>
        </div>
        <div className={classes.ps_info}>
          <div className={classes.ps_header}>
            <h3>Badges</h3>
            <abbr title="Info">
              <img
                src="/Assets/SVG/info.svg"
                className={classes.info_btn}
                alt="Info"
              />
            </abbr>
          </div>
          <div className={classes.ps_container}>
            <div className={classes.psi_badge}>
              <img src="/Assets/Images/apex-predator-rank.png" alt="Badge" />
              <span>Groofy Predator</span>
            </div>
            <div className={classes.psi_badge}>
              <img src="/Assets/Images/attackbadge.png" alt="Badge" />
              <span>High Accuracy</span>
            </div>
            <div className={classes.psi_badge}>
              <img src="/Assets/Images/win20badge.png" alt="Badge" />
              <span>Master Wins</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PInfo;
