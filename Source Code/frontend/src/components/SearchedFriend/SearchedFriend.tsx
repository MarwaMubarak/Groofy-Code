import { Toast } from "primereact/toast";
import { ProfileImage } from "..";
import { useDispatch } from "react-redux";
import { useRef, useState } from "react";
import { FriendProps } from "../../shared/types";
import { gameThunks, teamThunks } from "../../store/actions";
import classes from "./scss/searched-friend.module.css";

const SearchedFriend = (props: FriendProps) => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  const [isInvited, setIsInvited] = useState(props.isInvited);
  const [invitationId, setInvitationId] = useState(props.invitationId);

  const inviteFriend = async () => {
    return await dispatch(gameThunks.inviteToFriendlyGame(props.userId) as any);
  };

  const inviteToTeam = async () => {
    const res = await dispatch(
      teamThunks.InviteToTeam(props.teamId!, props.username) as any
    );
    setIsInvited(true);
    setInvitationId(res.body.invitationId);
    return res;
  };

  const cancelInviationToTeam = async () => {
    const res = await dispatch(
      teamThunks.CancelTeamInvitation(invitationId!) as any
    );
    setIsInvited(false);
    return res;
  };

  const cancelInvitation = async () => {
    return await dispatch(
      gameThunks.cancelInvitationToFriendlyGame(props.userId) as any
    );
  };

  return (
    <div className={classes.single_friend}>
      <Toast ref={toast} />
      <div className={classes.friend_info}>
        <ProfileImage
          photoUrl={props.photoUrl}
          username={props.username}
          style={{
            cursor: "default",
            backgroundColor: props.accountColor,
            width: "45px",
            height: "45px",
            fontSize: "20px",
          }}
          canClick={false}
        />
        <span className={classes.friend_name}>{props.username}</span>
      </div>
      <div className={classes.friend_actions}>
        {!isInvited ? (
          <button
            className={`${classes.friend_btn} ${classes.msg_btn}`}
            onClick={() => {
              if (props.teamId !== null && props.teamId !== undefined) {
                inviteToTeam();
              } else {
                inviteFriend();
              }
            }}
          >
            <i className="bi bi-person-fill-add"></i>
            <span>Invite</span>
          </button>
        ) : (
          <button
            className={`${classes.friend_btn} ${classes.remove_btn}`}
            onClick={() => {
              if (props.teamId !== null && props.teamId !== undefined) {
                cancelInviationToTeam();
              } else {
                cancelInvitation();
              }
            }}
          >
            <i className="bi bi-person-fill-x"></i>
            <span>Cancel Invitation</span>
          </button>
        )}
      </div>
    </div>
  );
};

export default SearchedFriend;
