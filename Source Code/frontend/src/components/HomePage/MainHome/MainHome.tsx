import GameModeCard from "./GameModeCard/GameModeCard";
import "./scss/mainhome.css";

const MainHome = () => {
  return (
    <div className="match-selector">
      <div className="ms-logo">
        <img
          className="logo"
          src="/Assets/Images/GroofyCodeLogo.png"
          alt="groofycode"
        />
      </div>
      <div className="ms-items">
        <GameModeCard
          cardImg="/Assets/Images/customize.png"
          cardTitle="Customize"
          cardButtonTitle="Battle"
          cardDescription="Empower players to create their perfect match by allowing them to customize every aspect."
          clickEvent={() => {}}
        />
        <GameModeCard
          cardImg="/Assets/Images/battle.png"
          cardTitle="Ranked"
          cardButtonTitle="Battle"
          cardDescription="Challenge your skills and climb the ranks with the option to play in a competitive ranked match."
          clickEvent={() => {}}
        />
        <GameModeCard
          cardImg="/Assets/Images/clan.png"
          cardTitle="Clan"
          cardButtonTitle="Join"
          cardDescription="Level up your gameplay and form alliances as you become part of a gaming community by joining clans."
          clickEvent={() => {}}
        />
      </div>
    </div>
  );
};

export default MainHome;
