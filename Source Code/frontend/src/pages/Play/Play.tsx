import { useSelector } from "react-redux";
import { Gamemode, GroofyHeader, SideBar } from "../../components";
import classes from "./scss/play.module.css";

const Play = () => {
  const user = useSelector((state: any) => state.auth.user);
  return (
    <div className={classes.play_page}>
      <SideBar idx={2} />
      <div className={classes.play_div}>
        <GroofyHeader />
        <div className={classes.play_mid_menu}>
          <div className={classes.play_left_menu}>
            <div className={classes.play_img_user}>
              <img src="/Assets/Images/profile_picture.jpg" alt="UserPhoto" />
            </div>
            <div className={classes.play_versus_img}>
              <img src="/Assets/Images/versus.png" alt="Versus" />
            </div>
            <div className={classes.play_img_user}>
              <img src={user.photo.url} alt="UserPhoto" />
            </div>
          </div>
          <div className={classes.play_menu}>
            <h3 className={classes.play_title}>Play a match</h3>
            <div className={classes.play_modes}>
              <Gamemode
                title="Velocity Code"
                img="/Assets/Images/clock.png"
                description="Play a 15-min match and be a fast coder."
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Casual Match"
                img="/Assets/Images/battle.png"
                description="Challenge others in a friendly competetion"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Ranked Match"
                img="/Assets/Images/ranked.png"
                description="Climb up the ranks to be the master coder"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Custom Match"
                img="/Assets/Images/customize.png"
                description="Play or create matches that suit your interests"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Beat a Friend"
                img="/Assets/Images/friends.png"
                description="Compete against your friends to find the better coder"
                type="full"
                clickEvent={() => {}}
              />
              <Gamemode
                title="2 Vs 2"
                img="/Assets/Images/coop.png"
                description="Team up with your friend against others"
                type="full"
                clickEvent={() => {}}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Play;
