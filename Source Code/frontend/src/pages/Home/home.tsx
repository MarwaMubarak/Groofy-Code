import {
  EventSection,
  FriendsSection,
  MainHome,
  NavBar
} from "../../components";
import "./scss/home.css";
// Bootstrap CSS
// import "bootstrap/dist/css/bootstrap.min.css";
// // Bootstrap Bundle JS
// import "bootstrap/dist/js/bootstrap.bundle.min";

const Home = () => {
  return(
    <div className="homepage">
      <NavBar/>
      <div className="mainhome">
        <MainHome/>
        <div className="actionsection">
          <EventSection/>
          <FriendsSection/>
        </div>
      </div>
  </div>
  );
};

export default Home;
