import { useState } from "react";
import { InputText } from "primereact/inputtext";
import { GroofyWrapper } from "../../components";
import { Button } from "primereact/button";
import classes from "./scss/clansearch.module.css";

const ClanSearch = () => {
  const [searchText, setSearchText] = useState<string>("");
  return (
    <GroofyWrapper idx={3}>
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
      </div>
    </GroofyWrapper>
  );
};

export default ClanSearch;
