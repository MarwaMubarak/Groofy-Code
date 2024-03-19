import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { InputText } from "primereact/inputtext";
import { GroofyHeader, SideBar } from "../../components";
import { Button } from "primereact/button";
import { Link } from "react-router-dom";
import { userThunks } from "../../store/actions";
import { AxiosError } from "axios";
import ReactCountryFlag from "react-country-flag";
import { Tooltip } from "primereact/tooltip";
import { Badge } from "primereact/badge";
import { Skeleton } from "primereact/skeleton";
import classes from "./scss/search.module.css";

interface Country {
  name: string;
  code: string;
}
const countries: Country[] = [
  { name: "Australia", code: "AU" },
  { name: "Brazil", code: "BR" },
  { name: "China", code: "CN" },
  { name: "Egypt", code: "EG" },
  { name: "France", code: "FR" },
  { name: "Germany", code: "DE" },
  { name: "India", code: "IN" },
  { name: "Japan", code: "JP" },
  { name: "Spain", code: "ES" },
  { name: "United States", code: "US" },
];

const Search = () => {
  const dispatch = useDispatch();
  const [loading, setLoading] = useState(false);
  const [searching, setSearching] = useState<boolean>(false);
  const [isTyping, setIsTyping] = useState<boolean>(false);
  const [searchText, setSearchText] = useState<string>("");
  const [searchedUsers, setSearchedUsers] = useState<any[]>([]);
  const load = () => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
    }, 2000);
  };

  useEffect(() => {
    if (searchText === "") {
      setSearching(false);
      setIsTyping(false);
      setSearchedUsers([]);
      return;
    }
    setSearching(true);
    setIsTyping(true);
    const typingTimeout = setTimeout(() => {
      setIsTyping(false);
    }, 1800);
    return () => clearTimeout(typingTimeout);
  }, [searchText]);

  useEffect(() => {
    if (!isTyping && searchText !== "") {
      const ret = dispatch(userThunks.searchForUsers(searchText) as any);
      if (ret instanceof Promise) {
        ret.then((res: any) => {
          console.log(res);
          if (res instanceof AxiosError) {
            console.log(res.response?.data?.message);
            setSearchedUsers([]);
          } else {
            console.log(res.data.message);
            setSearchedUsers(res.data.body);
          }
          setSearching(false);
        });
      }
    }
  }, [dispatch, isTyping, searchText]);

  return (
    <div className={classes.search_container}>
      <SideBar idx={6} />
      <div className={classes.search_page}>
        <GroofyHeader />
        <div className={classes.search_area}>
          <InputText
            placeholder="Search for users..."
            className={classes.prime_input}
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
          />
          <i className="pi pi-search" />
        </div>
        <div className={classes.search_box}>
          {searching && (
            <div className={classes.searching}>
              {[1, 2, 3, 4, 5].map(() => (
                <Skeleton
                  width="270px"
                  height="400px"
                  borderRadius="25px"
                ></Skeleton>
              ))}
            </div>
          )}
          {searchedUsers.length === 0 &&
            !searching &&
            (searchText === "" ? (
              <div></div>
            ) : (
              !searching &&
              searchText.length > 0 &&
              searchedUsers.length === 0 && (
                <div className={classes.nodata}>
                  <img src="/Assets/Images/nodata.png" alt="No Data" />
                  <span>User not found.</span>
                </div>
              )
            ))}
          {searchedUsers.length > 0 &&
            !searching &&
            searchedUsers.map((user) => (
              <div className={classes.search_result}>
                <div className={classes.user_skin}></div>
                <div className={classes.message_user}>
                  <Tooltip
                    target=".custom-target-icon"
                    style={{ fontSize: "12px" }}
                  />
                  <i
                    className={`custom-target-icon pi pi-envelope p-text-secondary p-overlay-badge ${classes.user_msg}`}
                    data-pr-tooltip="Send a message"
                    data-pr-position="right"
                    data-pr-at="right+5 top"
                    data-pr-my="left center-2"
                  >
                    <Badge severity="danger"></Badge>
                  </i>
                </div>
                <Link to={`/profile/${user.username}`}>
                  <div className={classes.sr_user}>
                    <img
                      src={`${user.photoUrl}`}
                      alt="ProfilePicture"
                      className={classes.user_img}
                    />
                    <div className={classes.sr_username}>
                      <h3>{user.username}</h3>
                      {user.country && user.country !== "" && (
                        <ReactCountryFlag
                          countryCode={
                            countries.find(
                              (country) => country.name === user.country
                            )?.code!
                          }
                          className={classes.user_flag}
                          svg
                          title={user.country}
                        />
                      )}
                    </div>
                  </div>
                </Link>
                <div className={classes.user_info}>
                  <div className={classes.user_divisions}>
                    <div className={classes.division}>
                      <h3>none</h3>
                      <span>World Rank</span>
                    </div>
                    <div className={classes.division}>
                      <h3>5030</h3>
                      <span>Max Trophies</span>
                    </div>
                    <div className={classes.division}>
                      <h3>0</h3>
                      <span>wins</span>
                    </div>
                    <div className={classes.division}>
                      <h3>0</h3>
                      <span>losses</span>
                    </div>
                  </div>
                  <div className={classes.sr_controls}>
                    <Button
                      icon="pi pi-user-plus"
                      label="Add Friend"
                      rounded
                      raised
                      loading={loading}
                      onClick={load}
                    />
                  </div>
                </div>
              </div>
            ))}
        </div>
      </div>
    </div>
  );
};

export default Search;
