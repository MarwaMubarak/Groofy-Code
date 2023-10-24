import "./scss/eventsection.css"

const EventSection = () => {
    return(
      <div className="events-background">
          <div className="events-header">
            <img src="./Assets/Events.png" alt="Events"/>
            <h2>Upcoming Events</h2>
          </div>
          <ul className="events-body">
            <li>
              <h3>
                Event 1
              </h3>
            </li>
            <li>
              <h3>
                Event2
              </h3>
            </li>
          </ul>
      </div>
    );
  };
  
  export default EventSection;
  