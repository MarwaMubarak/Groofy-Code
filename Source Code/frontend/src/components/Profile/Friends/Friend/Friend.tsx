import { useDispatch } from "react-redux";
import { FriendProps } from "../../../../shared/types";
import classes from "./scss/single-friend.module.css";
import { friendThunks } from "../../../../store/actions";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";

const Friend = (props: FriendProps) => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);

  const removeFriend = async () => {
    return await dispatch(friendThunks.RemoveFriend(props.userId) as any);
  };

  const confirmRemoveFriend = () => {
    confirmDialog({
      message: "Are you sure you want to remove this friend ?",
      header: "Confirmation",
      icon: "pi pi-exclamation-triangle",
      accept: () =>
        removeFriend()
          .then((res: any) => {
            if (res.status === "success") {
              toast.current?.show({
                severity: "success",
                summary: res.status,
                detail: res.message,
                life: 3000,
              });
            }
          })
          .catch((error: any) => {
            toast.current?.show({
              severity: "error",
              summary: error.status,
              detail: error.message,
              life: 3000,
            });
          }),
      reject: () => null,
    });
  };

  return (
    <div className={classes.single_friend}>
      <Toast ref={toast} />
      <ConfirmDialog />
      <div className={classes.friend_info}>
        <img
          src={props.photoUrl}
          alt="Profile"
          className={classes.friend_profile}
        />
        <span className={classes.friend_name}>{props.username}</span>
      </div>
      <div className={classes.friend_actions}>
        <button className={`${classes.friend_btn} ${classes.msg_btn}`}>
          <i className="bi bi-chat-dots-fill"></i>
          <span>Message</span>
        </button>
        <button
          className={`${classes.friend_btn} ${classes.remove_btn}`}
          onClick={confirmRemoveFriend}
        >
          <i className="bi bi-person-dash-fill"></i>
          <span>Remove</span>
        </button>
      </div>
    </div>
  );
};

export default Friend;
