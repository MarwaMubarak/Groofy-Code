import { cardProps } from "../../../../shared/types";
import GBtn from "../../../GBtn/GBtn";
import "./scss/gamemodecard.css"
const GameModeCard = (props : cardProps) => {
    return(
    <div className="cardContainer">
        <img className = "cardImg" src={props.cardImg} alt="cardImg"/>
        <h2 className = "cardTitle"> {props.cardTitle} </h2>
        <p className = "cardDescription"> {props.cardDescription} </p>
        <div className="btn-container">
          <GBtn btnText = {props.cardButtonTitle} clickEvent={()=>{}}/>
        </div>
    </div>
    );
  };
  
export default GameModeCard;
  