import { Toast } from "primereact/toast";
import { ProfileImage } from "..";
import { useDispatch } from "react-redux";
import { useRef } from "react";
import { FriendProps } from "../../shared/types";
import { gameThunks } from "../../store/actions";
import classes from "./scss/searched-friend.module.css";

const SearchedFriend = (props: FriendProps) => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);

  const inviteFriend = async () => {
    return await dispatch(gameThunks.inviteToFriendlyGame(props.userId) as any);
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
        {!props.isInvited ? (
          <button
            className={`${classes.friend_btn} ${classes.msg_btn}`}
            onClick={inviteFriend}
          >
            <i className="bi bi-person-fill-add"></i>
            <span>Invite</span>
          </button>
        ) : (
          <button
            className={`${classes.friend_btn} ${classes.remove_btn}`}
            onClick={cancelInvitation}
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
