import { useEffect, useState } from "react";
import { Friends, History, PostsContainer } from "../..";
import { PSocialProps } from "../../../shared/types";
import classes from "./scss/psocial.module.css";

const PSocial = (props: PSocialProps) => {
  const [activeTab, setActiveTab] = useState<number>(0);

  useEffect(() => {
    setActiveTab(0);
  }, [props.profName]);

  return (
    <div className={classes.up_side_left}>
      <div className={classes.media_section}>
        <div
          className={`${classes.media_selectors} ${
            props.profName !== props.loggedUser && classes.false
          }`}
        >
          <div
            className={`${classes.ms} ${activeTab === 0 && classes.active}`}
            onClick={() => setActiveTab(0)}
          >
            <h3>Posts</h3>
          </div>
          {props.profName === props.loggedUser && (
            <div
              className={`${classes.ms} ${activeTab === 1 && classes.active}`}
              onClick={() => setActiveTab(1)}
            >
              <h3>Friends</h3>
            </div>
          )}

          <div
            className={`${classes.ms} ${activeTab === 2 && classes.active}`}
            onClick={() => setActiveTab(2)}
          >
            <h3>Clan</h3>
          </div>
          <div
            className={`${classes.ms} ${activeTab === 3 && classes.active}`}
            onClick={() => setActiveTab(3)}
          >
            <h3>History</h3>
          </div>
        </div>
        {activeTab === 0 && (
          <PostsContainer
            user={props.profileUser}
            toast={props.toast}
            self={false}
          />
        )}
        {activeTab === 1 && <Friends />}
        {activeTab === 2 && <div>Clan</div>}
        {activeTab === 3 && <History />}
      </div>
    </div>
  );
};

export default PSocial;
