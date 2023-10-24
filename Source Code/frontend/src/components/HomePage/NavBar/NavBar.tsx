import "./scss/navbar.css"
const NavBar = () => {
  return(
  <div className="slider-style">
    <ul className="icons-style">
        <li className="active">
            <img className = "icon" src="/Assets/HomeIcon.png" alt="NavBarHome"/>
        </li>  
        <li>
            <img className = "icon" src="/Assets/ProfileIcon.png" alt="NavBarProfile"/>
        </li>
        <li>
            <img className = "icon" src="/Assets/BattleIcon.png" alt="NavBarBattle"/>
        </li>
        <li>
            <img className = "iconClan" src="/Assets/ClanIcon.png" alt="NavBarClan"/>
        </li>
        <li>
            <img className = "icon" src="/Assets/NewsIcon.png" alt="NavBarNews"/>
        </li>
        <li>
            <img className = "icon" src="/Assets/HelpIcon.png" alt="NavBarHelp"/>
        </li>
    </ul>
  </div>
  );
};

export default NavBar;
