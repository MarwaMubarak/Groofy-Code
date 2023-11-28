import GBtn from "../../GBtn/GBtn";
import "./scss/eventcard.css";
import { EventCardProps } from "../../../shared/types";
const EventCard = (props: EventCardProps) => {
  return (
    <div className="event-card">
      <h3>{props.title}</h3>
      <div className="ec-details">
        <p>{props.details}</p>
        <GBtn btnText={props.btn_title} clickEvent={()=>{}}/>
      </div>
    </div>
  );
};

export default EventCard;
