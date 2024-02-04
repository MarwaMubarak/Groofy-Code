import { Button } from "primereact/button";
import classes from "./scss/clanresult.module.css";

const ClanResult = () => {
  return (
    <div className={classes.clan_result}>
      <div className={classes.clan_img}>
        <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
      </div>
      <div className={classes.clan_info}>
        <h3>Clan Name</h3>
        <div className={classes.clan_stats}>
          <span>Members: 87/100</span>
          <span>World Rank: #10</span>
          <span>Wins: 54</span>
          <span>Losses: 12</span>
        </div>
        <div className={classes.clan_actions}>
          <Button label="View" />
          <Button label="Join" />
        </div>
      </div>
    </div>
  );
};

export default ClanResult;
