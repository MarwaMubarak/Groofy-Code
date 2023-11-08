import {
  EventSection,
  FriendsSection,
  GroofyFooter,
  GroofyHeader,
  MainHome,
  NavBar,
} from "../../components";
import "./scss/home.css";

const Home = () => {
  return (
    <div className="home-div align">
      <GroofyHeader />
      <div className="h-wrapper">
        <NavBar idx={0} />
        <div className="h-content">
          <MainHome />
          <div className="activities-section">
            <EventSection />
            <FriendsSection />
          </div>
        </div>
      </div>
      <GroofyFooter />
    </div>
  );
};

export default Home;
