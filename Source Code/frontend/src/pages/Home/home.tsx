import {
  EventSection,
  FriendsSection,
  GroofyFooter,
  GroofyHeader,
  MainHome,
  NavBar,
} from "../../components";
import "./scss/home.css";
// Bootstrap CSS
// import "bootstrap/dist/css/bootstrap.min.css";
// // Bootstrap Bundle JS
// import "bootstrap/dist/js/bootstrap.bundle.min";

const Home = () => {
  return (
    <>
      <div className="home-container">
        <GroofyHeader/>
        <div className="mainhome">
          <NavBar idx={0} />
          <MainHome />
          <div className="actionsection">
            <EventSection />
            <FriendsSection />
          </div>
        </div>
        <GroofyFooter/>
      </div>
    </>
  );
};

export default Home;
