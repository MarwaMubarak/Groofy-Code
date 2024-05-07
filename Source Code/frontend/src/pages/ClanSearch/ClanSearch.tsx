import { useState } from "react";
import { InputText } from "primereact/inputtext";
import { GroofyHeader, GroofyWrapper } from "../../components";
import { Button } from "primereact/button";
import classes from "./scss/clansearch.module.css";

const ClanSearch = () => {
  const [searchText, setSearchText] = useState<string>("");
  return (
    <GroofyWrapper idx={3}>
      <div className={classes.page_container}>
        <GroofyHeader />
        <div className={classes.clan_search}>
          <div className={classes.search_area}>
            <InputText
              placeholder="Search for clans..."
              className={classes.prime_input}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
            />
            <i className="pi pi-search" />
            <Button
              label="Create Clan"
              icon="bi bi-plus-lg"
              className={classes.create_btn}
            />
          </div>
          <div className={classes.clan_results}>
            <div className={classes.clan_card}>
              <div className={classes.clan_info}>
                <div className={classes.clan_img}>
                  <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
                </div>
                <div className={classes.clan_details}>
                  <div className={classes.clan_name}>The Elites</div>
                  <div className={classes.clan_tags}>
                    <span>Members: 4/10</span>
                    <span>World Rank: #10</span>
                    <span>Wins: 10</span>
                    <span>Losses: 7</span>
                  </div>
                </div>
              </div>
              <div className={classes.clan_actions}>
                <Button
                  label="Join"
                  icon="bi bi-plus-lg"
                  className={classes.join_btn}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </GroofyWrapper>
  );
};

export default ClanSearch;
