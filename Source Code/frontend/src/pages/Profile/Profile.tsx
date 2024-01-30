import { useEffect, useRef } from "react";
import { SideBar, GroofyHeader, GBtn, PostsContainer } from "../../components";
import { useSelector } from "react-redux";
import ReactCountryFlag from "react-country-flag";
import { Toast } from "primereact/toast";
import { Image } from "primereact/image";
import { postActions } from "../../store/slices/post-slice";
import { userThunks } from "../../store/actions";
import { useDispatch } from "react-redux";
import { Link, useParams } from "react-router-dom";
import FormatDate from "../../shared/functions/format-date";
import classes from "./scss/profile.module.css";

interface Country {
  name: string;
  code: string;
}

const Profile = () => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  const user = useSelector((state: any) => state.auth.user);
  dispatch(postActions.setStatus(""));
  dispatch(postActions.setMessage(""));
  const { username: userProfile } = useParams();
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

  useEffect(() => {
    const getUser = async () => {
      await dispatch(userThunks.getUser(userProfile!) as any);
    };
    getUser();
  }, [dispatch, userProfile]);

  return (
    <div className={classes.newprofile_container}>
      <Toast ref={toast} />
      <SideBar idx={1} />
      <div className={classes.userprofile}>
        <GroofyHeader />
        <div className={classes.up_info}>
          <div className={classes.up_info_img}>
            <Image src={user.photo.url} alt="Image" width="160" preview />
          </div>
          <div className={classes.up_info_details}>
            <div className={classes.up_info_d_box}>
              <h3>{user.username}</h3>
              {userProfile === user.username && (
                <Link to="/profile/edit">
                  <div className={classes.up_info_d_box_edit}>
                    <img src="/Assets/SVG/edit.svg" alt="EditBtn" />
                    <span>Edit</span>
                  </div>
                </Link>
              )}
            </div>
            <h4>
              {user.firstname + " " + user.lastname}, {user.city || ""},{" "}
              {user.country || ""}
              <ReactCountryFlag
                countryCode={
                  countries.find((country) => country.name === user.country)
                    ?.code || " "
                }
                svg
                style={{
                  width: "1em",
                  height: "1em",
                  marginLeft: "8px",
                }}
                title={user.country || ""}
              />
            </h4>
            <p>{user.bio || ""}</p>
            {userProfile !== user.username && (
              <div className={classes.up_info_details_controls}>
                <GBtn
                  btnText="Message"
                  icnSrc="/Assets/SVG/message.svg"
                  clickEvent={() => {}}
                />
                <GBtn
                  btnText="Add Friend"
                  icnSrc="/Assets/SVG/addfriend.svg"
                  clickEvent={() => {}}
                />
              </div>
            )}
          </div>
        </div>
        <div className={classes.userprofile_side}>
          <div className={classes.up_side_left}>
            <div className={classes.media_section}>
              <div
                className={`${classes.media_selectors} ${
                  userProfile !== user.username && classes.false
                }`}
              >
                <div className={`${classes.ms + " " + classes.active}`}>
                  <h3>Posts</h3>
                </div>
                {userProfile === user.username && (
                  <div className={classes.ms}>
                    <h3>Friends</h3>
                  </div>
                )}

                <div className={classes.ms}>
                  <h3>Clan</h3>
                </div>
                <div className={classes.ms}>
                  <h3>History</h3>
                </div>
              </div>
              <PostsContainer toast={toast} self={false} />
            </div>
          </div>
          <div className={classes.up_side_right}>
            <div className={classes.profile_section}>
              <div className={classes.ps_info}>
                <div className={classes.ps_header}>
                  <h3>Info</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className={classes.info_btn}
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className={classes.ps_container_box}>
                  <div className={classes.psi_single_details}>
                    <span>
                      <img src="/Assets/SVG/calendar.svg" alt="calender" />
                      Joined
                      <span className={classes.beside}>
                        {FormatDate(user.createdAt)}
                      </span>
                    </span>
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>
                      World Rank:{" "}
                      <span className={classes.beside}> {user.rank}</span>
                    </span>
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>
                      Last Seen:
                      <span className={classes.ls}>
                        {user.isOnline ? "Online" : "Offline"}
                      </span>
                    </span>
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>
                      Friends:
                      <span className={classes.friends}>
                        {user.friends?.length || 0}
                      </span>
                    </span>
                  </div>
                </div>
              </div>
              <div className={classes.ps_info}>
                <div className={classes.ps_header}>
                  <h3>Statistics</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className={classes.info_btn}
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className={classes.ps_container_box}>
                  <div className={classes.psi_single_details}>
                    <span>
                      <img
                        src="/Assets/Images/battleicon.png"
                        alt="StatsIcon"
                      />
                      Total Matches
                    </span>
                    <span className={classes.any}>{user.totalMatch}</span>
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>
                      <img
                        src="/Assets/Images/Yellow_trophy.png"
                        alt="StatsIcon"
                      />
                      Highest Trophies
                    </span>
                    <span className={classes.any}>5030</span>
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>Wins</span>
                    <span className={classes.any}>{user.wins}</span>
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>Loses</span>
                    <span className={classes.any}>{user.loses}</span>
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>Draws</span>
                    <span className={classes.any}>{user.draws}</span>
                  </div>
                </div>
              </div>
              <div className={classes.ps_info}>
                <div className={classes.ps_header}>
                  <h3>Division</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className={classes.info_btn}
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className={classes.ps_container}>
                  <div className={classes.psi_box}>
                    <img src="/Assets/Images/elite-rank.png" alt="RankImg" />
                    <div className={classes.wrapper}>
                      <span>Rank</span>
                      <h3>Elite</h3>
                    </div>
                  </div>
                  <div className={classes.psi_box}>
                    <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
                    <div className={classes.wrapper}>
                      <span>Clan</span>
                      <h3>Ghosts</h3>
                    </div>
                  </div>
                </div>
              </div>
              <div className={classes.ps_info}>
                <div className={classes.ps_header}>
                  <h3>Badges</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className={classes.info_btn}
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className={classes.ps_container}>
                  <div className={classes.psi_badge}>
                    <img
                      src="/Assets/Images/apex-predator-rank.png"
                      alt="Badge"
                    />
                    <span>Groofy Predator</span>
                  </div>
                  <div className={classes.psi_badge}>
                    <img src="/Assets/Images/attackbadge.png" alt="Badge" />
                    <span>High Accuracy</span>
                  </div>
                  <div className={classes.psi_badge}>
                    <img src="/Assets/Images/win20badge.png" alt="Badge" />
                    <span>Master Wins</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
