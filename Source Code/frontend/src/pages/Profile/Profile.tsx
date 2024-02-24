import { useEffect, useRef } from "react";
import { SideBar, GroofyHeader, GBtn, PSocial, PInfo } from "../../components";
import { useSelector } from "react-redux";
import ReactCountryFlag from "react-country-flag";
import { Toast } from "primereact/toast";
import { Image } from "primereact/image";
import { postActions } from "../../store/slices/post-slice";
import { userThunks } from "../../store/actions";
import { useDispatch } from "react-redux";
import { Link, useParams } from "react-router-dom";
import classes from "./scss/profile.module.css";
import { Button } from "primereact/button";
import FormatDate from "../../shared/functions/format-date";

interface Country {
  name: string;
  code: string;
}

const Profile = () => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  const loggedUser = useSelector((state: any) => state.auth.user);
  const profileUser = useSelector((state: any) => state.user.user);
  const profileRes = useSelector((state: any) => state.user.res);
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
        {profileRes.status === "success" ? (
          <>
            <div className={classes.up_info}>
              <div className={classes.left_up_info}>
                <div className={classes.up_info_img}>
                  <Image
                    src={profileUser.photo.url}
                    alt="Image"
                    width="180"
                    preview
                  />
                </div>
                <div className={classes.up_info_details}>
                  <div className={classes.up_info_d_box}>
                    <div className={classes.up_info_d_box_names}>
                      <h3>
                        {profileUser.firstname + " " + profileUser.lastname}
                        {profileUser.country && profileUser.country !== "" && (
                          <ReactCountryFlag
                            countryCode={
                              countries.find(
                                (country) =>
                                  country.name === profileUser.country
                              )?.code || " "
                            }
                            svg
                            style={{
                              width: "1em",
                              height: "1em",
                              marginLeft: "8px",
                            }}
                            title={profileUser.country || ""}
                          />
                        )}
                      </h3>
                      <h4>@{profileUser.username}</h4>
                    </div>
                    {userProfile === loggedUser.username && (
                      <Link to="/profile/edit">
                        <div className={classes.up_info_d_box_edit}>
                          <img src="/Assets/SVG/edit.svg" alt="EditBtn" />
                          <span>Edit</span>
                        </div>
                      </Link>
                    )}
                    {userProfile !== loggedUser.username && (
                      <div className={classes.up_info_details_controls}>
                        <button
                          className={
                            "bi bi-chat-dots-fill " + classes.user_action_btn
                          }
                        />
                        <button
                          className={
                            "bi bi-person-plus-fill " + classes.user_action_btn
                          }
                        />
                      </div>
                    )}
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>
                      <img src="/Assets/SVG/calendar.svg" alt="calender" />
                      Joined
                      <span className={classes.beside}>
                        {FormatDate(
                          userProfile === loggedUser.username
                            ? loggedUser.createdAt
                            : profileUser.createdAt
                        )}
                      </span>
                    </span>
                  </div>

                  <p>{profileUser.bio || ""}</p>
                </div>
              </div>
              <div className={classes.middle_up_info}>
                <div className={classes.ps_header}>
                  <h3>Division</h3>
                </div>
                <div className={classes.middle_upper_section}>
                  <div className={classes.rank_img}>
                    <img src="/Assets/Badges/Badge5.svg" alt="RankImg" />
                  </div>
                  <div className={classes.rank_info}>
                    <h3>Elite</h3>
                    <div className={classes.rank_box}>
                      <img
                        src="/Assets/SVG/trophyIconYellow.svg"
                        alt="RankImg"
                      />
                      <span>1986</span>
                    </div>
                  </div>
                </div>
                <div className={classes.middle_down_section}>
                  <div className={classes.middle_down_info}>
                    <h3>World rank</h3>
                    <div className={classes.middle_down_box}>
                      <img src="/Assets/SVG/world_rank.svg" alt="RankImg" />
                      <span>#217</span>
                    </div>
                  </div>
                  <div className={classes.middle_down_info}>
                    <h3>Max Rating</h3>
                    <div className={classes.middle_down_box}>
                      <img src="/Assets/Badges/Badge6.svg" alt="RankImg" />
                      <span>3247</span>
                    </div>
                  </div>
                </div>
              </div>
              <div className={classes.right_up_info}>
                <div className={classes.ps_info}>
                  <div className={classes.ps_header}>
                    <h3>Clan</h3>
                  </div>
                  <div className={classes.ps_container}>
                    <div className={classes.psi_box}>
                      <img src="/Assets/Badges/Badge1.svg" alt="ClanImg" />
                      <div className={classes.wrapper}>
                        <span>Clan</span>
                        <h3>Ghosts</h3>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className={classes.userprofile_side}>
              <PSocial
                profName={userProfile!}
                loggedUser={loggedUser.username}
                profileUser={profileUser}
                toast={toast}
              />
              <PInfo profileUser={profileUser} />
            </div>
          </>
        ) : profileRes.status !== "failure" ? (
          <div>Loading...</div>
        ) : (
          <div>{profileRes.message}</div>
        )}
      </div>
    </div>
  );
};

export default Profile;
