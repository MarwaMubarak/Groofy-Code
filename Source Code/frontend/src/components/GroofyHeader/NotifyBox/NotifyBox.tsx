import { useDispatch, useSelector } from "react-redux";
import { ProfileImage } from "../..";
import { NotifyBoxProps } from "../../../shared/types";
import classes from "./scss/notifybox.module.css";
import {
  friendThunks,
  gameThunks,
  notifyThunks,
  teamThunks,
} from "../../../store/actions";

const NotifyBox = (props: NotifyBoxProps) => {
  const dispatch = useDispatch();
  const loggedUser = useSelector((state: any) => state.auth.user);

  const handleAccept = () => {
    if (props.nType === "FRIEND_REQUEST") {
      dispatch(friendThunks.AcceptFriendRequest(props.nuid) as any);
    } else if (props.nType === "FRIEND_MATCH_INVITATION") {
      dispatch(
        gameThunks.acceptFriendlyGameInvitation(
          props.nInvId,
          loggedUser.id
        ) as any
      );
    } else if (props.nType === "TEAM_INVITATION") {
      dispatch(teamThunks.AcceptTeamInvitation(props.nInvId) as any);
    } else if (props.nType === "MATCH_INVITATION") {
      dispatch(teamThunks.AcceptTeamGameInvitation(props.nInvId) as any);
    }
    dispatch(notifyThunks.removeNotification(props.nid) as any);
  };

  const handleReject = () => {
    if (props.nType === "FRIEND_REQUEST") {
      dispatch(friendThunks.RejectFriendRequest(props.nuid) as any);
    } else if (props.nType === "FRIEND_MATCH_INVITATION") {
      dispatch(
        gameThunks.rejectFriendlyGameInvitation(
          props.nInvId,
          loggedUser.id
        ) as any
      );
    } else if (props.nType === "TEAM_INVITATION") {
      dispatch(teamThunks.RejectTeamInvitation(props.nInvId) as any);
    } else if (props.nType === "MATCH_INVITATION") {
      dispatch(teamThunks.RejectTeamGameInvitation(props.nInvId) as any);
    }
    dispatch(notifyThunks.removeNotification(props.nid) as any);
  };

  return (
    <div className={classes.notify_box}>
      <div className={classes.notify_info}>
        <div className={classes.notify_img}>
          <ProfileImage
            photoUrl={props.nuImg}
            username={props.nusn}
            style={{
              cursor: "pointer",
              backgroundColor: props.ncolor,
              width: "45px",
              height: "45px",
              fontSize: "20px",
            }}
            canClick={false}
          />
        </div>
        <div className={classes.notify_user}>
          <span>
            <span>{props.nusn}</span> {props.nbody}
          </span>
          <p>{props.ntime}</p>
        </div>
      </div>
      {props.nType !== "SEND_LIKE" &&
        props.nType !== "FRIEND_ACCEPT" &&
        props.nType === "MATCH_INVITATION" &&
        props.nIsAdmin === true && (
          <div className={classes.notify_btns}>
            <img
              className={classes.acc_btn}
              src="/Assets/SVG/acceptIcon.svg"
              alt="accept"
              onClick={handleAccept}
            />
            <img
              className={classes.cancel_btn}
              src="/Assets/SVG/cancelIcon.svg"
              alt="cancel"
              onClick={handleReject}
            />
          </div>
        )}
    </div>
  );
};

export default NotifyBox;
