import { GamemodeProps } from "../../../shared/types";
import classes from "./scss/gamemode.module.css";

const Gamemode = (props: GamemodeProps) => {
  console.log("MY ID", props.id);
  return props.description ? (
    <div
      id={props.id ? props.id : ""}
      className={`${
        props.type === "full" ? classes.gamemode_full : classes.gamemode
      }`}
      onClick={props.clickEvent}
    >
      <img src={props.img} alt="Gamemode" />
      <div
        className={`${
          props.type === "full" ? classes.gm_info_full : classes.gm_info
        }`}
      >
        <h3>{props.title}</h3>
        <p>{props.description}</p>
      </div>
    </div>
  ) : (
    <div
      id={props.id ? props.id : ""}
      className={classes.mini_gamemode}
      onClick={props.clickEvent}
    >
      <img src={props.img} alt="Gamemode" />
      <h3>{props.title}</h3>
    </div>
  );
};

export default Gamemode;
