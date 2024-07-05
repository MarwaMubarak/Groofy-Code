import { useDispatch, useSelector } from "react-redux";
import classes from "./scss/friends.module.css";
import { friendThunks } from "../../../store/actions";
import { useEffect } from "react";
import { Friend } from "../..";

const Friends = () => {
  const dispatch = useDispatch();
  const friends: any[] = useSelector((state: any) => state.friend.friends);

  useEffect(() => {
    const getFriends = async () => {
      await dispatch(friendThunks.GetFriends() as any);
    };

    getFriends();
  }, [dispatch]);

  console.log("Friends", friends);

  return (
    <div className={classes.friends}>
      {(friends === null || friends?.length === 0) && (
        <h3>You have no friends</h3>
      )}
      {friends !== null &&
        friends?.map((friend: any, idx: number) => (
          <Friend
            key={idx}
            userId={friend.friendId}
            username={friend.username}
            photoUrl={friend.photoUrl}
            accountColor={friend.accountColor}
          />
        ))}
    </div>
  );
};

export default Friends;
