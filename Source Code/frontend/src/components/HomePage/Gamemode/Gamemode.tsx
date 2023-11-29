import { GamemodeProps } from "../../../shared/types";
import "./scss/gamemode.css";

const Gamemode = (props: GamemodeProps) => {
  return props.description ? (
    <div className="gamemode" onClick={props.clickEvent}>
      <img src={props.img} alt="Gamemode" />
      <div className="gm-info">
        <h3>{props.title}</h3>
        <p>{props.description}</p>
      </div>
    </div>
  ) : (
    <div className="mini-gamemode" onClick={props.clickEvent}>
      <img src={props.img} alt="Gamemode" />
      <h3>{props.title}</h3>
    </div>
  );
};

export default Gamemode;
