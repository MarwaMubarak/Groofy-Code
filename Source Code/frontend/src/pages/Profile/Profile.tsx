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
              <div className={classes.up_info_img}>
                <Image
                  src={profileUser.photo.url}
                  alt="Image"
                  width="160"
                  preview
                />
              </div>
              <div className={classes.up_info_details}>
                <div className={classes.up_info_d_box}>
                  <h3>{profileUser.username}</h3>
                  {userProfile === loggedUser.username && (
                    <Link to="/profile/edit">
                      <div className={classes.up_info_d_box_edit}>
                        <img src="/Assets/SVG/edit.svg" alt="EditBtn" />
                        <span>Edit</span>
                      </div>
                    </Link>
                  )}
                </div>
                <h4>
                  {(profileUser.firstname || "") +
                    " " +
                    (profileUser.lastname || "")}
                  {profileUser.city ? "," + profileUser.city + "," : ""}
                  {profileUser.country || ""}
                  {profileUser.country && profileUser.country !== "" && (
                    <ReactCountryFlag
                      countryCode={
                        countries.find(
                          (country) => country.name === profileUser.country
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
                </h4>
                <p>{profileUser.bio || ""}</p>
                {userProfile !== loggedUser.username && (
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
