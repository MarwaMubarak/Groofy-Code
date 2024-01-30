import { useRef } from "react";
import { Toast } from "primereact/toast";
import {
  GroofyHeader,
  SideBar,
  Gamemode,
  PostsContainer,
} from "../../components";
import { postActions } from "../../store/slices/post-slice";
import { useDispatch } from "react-redux";
import classes from "./scss/home.module.css";

const Home = () => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  dispatch(postActions.setStatus(""));
  dispatch(postActions.setMessage(""));
  return (
    <div className={classes.home_container}>
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
      <SideBar idx={0} />
      <div className={classes.activity_section}>
        <GroofyHeader />
        <div className={classes.play_section}>
          <div className={classes.play_section_features}>
            <h3 className={classes.psf_header}>Play</h3>
            <div className={classes.play_container}>
              <Gamemode
                title="Velocity Code"
                description="Face off in a 15-minute coding duel. Strategize, code swiftly, and emerge victorious in this high-stakes test of programming prowess."
                img="/Assets/Images/clock.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Ranked Match"
                description="Climb the coding ranks in intense head-to-head battles. Prove your skills, rise to the top, and become the coding champion."
                img="/Assets/Images/ranked.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Join Clan"
                description="Level up your gamplay and form alliances as you become a part of a gaming community by joining a clan."
                img="/Assets/Images/clan.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Solo Practice"
                description="Sharpen your skills and prepare for battle by practicing against a computer opponent."
                img="/Assets/Images/lightbulb.png"
                clickEvent={() => {}}
              />
            </div>
          </div>
          <div className={classes.play_section_gamemode}>
            <h3 className={classes.psg_header}>Game Modes</h3>
            <div className={classes.gamemode_box}>
              <Gamemode
                title="Casual Match"
                img="/Assets/Images/battle.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Custom Match"
                img="/Assets/Images/customize.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="2 Vs 2"
                img="/Assets/Images/coop.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Beat a Friend"
                img="/Assets/Images/friends.png"
                clickEvent={() => {}}
              />
            </div>
          </div>
        </div>
        <div className={classes.media_section}>
          <div className={classes.home_posts_container}>
            <h3 className={classes.hpc_title}>Posts</h3>
            <PostsContainer toast={toast} self={true} />
          </div>
          <div className={classes.profile_section}>
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
                  <img
                    src="/Assets/Images/apex-predator-rank.png"
                    alt="Badge"
                  />
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
      </div>
    </div>
  );
};

export default Home;
