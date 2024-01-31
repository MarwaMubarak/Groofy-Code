import { PostsContainer } from "../..";
import { PSocialProps } from "../../../shared/types";
import classes from "./scss/psocial.module.css";

const PSocial = (props: PSocialProps) => {
  return (
    <div className={classes.up_side_left}>
      <div className={classes.media_section}>
        <div
          className={`${classes.media_selectors} ${
            props.profName !== props.loggedUser && classes.false
          }`}
        >
          <div className={`${classes.ms + " " + classes.active}`}>
            <h3>Posts</h3>
          </div>
          {props.profName === props.loggedUser && (
            <div className={classes.ms}>
              <h3>Friends</h3>
            </div>
          )}

          <div className={classes.ms}>
            <h3>Clan</h3>
          </div>
          <div className={classes.ms}>
            <h3>History</h3>
          </div>
        </div>
        <PostsContainer
          user={props.profileUser}
          toast={props.toast}
          self={false}
        />
      </div>
    </div>
  );
};

export default PSocial;
