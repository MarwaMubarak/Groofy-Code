import { useDispatch, useSelector } from "react-redux";
import { PInfoProps } from "../../../shared/types";
import { useEffect } from "react";
import { friendThunks } from "../../../store/actions";
import classes from "./scss/pinfo.module.css";

const PInfo = (props: PInfoProps) => {
  const dispatch = useDispatch();
  const friendsCnt = useSelector((state: any) => state.friend.friendsCnt);

  useEffect(() => {
    const getFriendsCnt = async () => {
      await dispatch(friendThunks.GetFriendsCount(props.profileUser.id) as any);
    };

    getFriendsCnt();
  }, [dispatch, props.profileUser.id]);

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
            {/* <div className={classes.psi_single_details}>
              <span>
                Last Seen:
                <span className={classes.ls}>
                  {props.profileUser.isOnline ? "Online" : "Offline"}
                </span>
              </span>
            </div> */}
            <div className={classes.psi_single_details}>
              <span>
                Friends:
                <span className={classes.friends}>{friendsCnt}</span>
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
                {props.profileUser.wins +
                  props.profileUser.losses +
                  props.profileUser.draws}
              </span>
            </div>
            <div className={classes.psi_single_details}>
              <span>Wins</span>
              <span className={classes.any}>{props.profileUser.wins}</span>
            </div>
            <div className={classes.psi_single_details}>
              <span>Loses</span>
              <span className={classes.any}>{props.profileUser.losses}</span>
            </div>
            <div className={classes.psi_single_details}>
              <span>Draws</span>
              <span className={classes.any}>{props.profileUser.draws}</span>
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
