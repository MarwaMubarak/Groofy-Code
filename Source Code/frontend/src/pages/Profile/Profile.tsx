import { useEffect, useRef, useState } from "react";
import {
  SideBar,
  GroofyHeader,
  PSocial,
  PInfo,
  ProfileImage,
} from "../../components";
import { useSelector } from "react-redux";
import ReactCountryFlag from "react-country-flag";
import { Toast } from "primereact/toast";
import { Image } from "primereact/image";
import { postActions } from "../../store/slices/post-slice";
import { userThunks, friendThunks } from "../../store/actions";
import { useDispatch } from "react-redux";
import { Link, useParams } from "react-router-dom";
import classes from "./scss/profile.module.css";
import FormatDate from "../../shared/functions/format-date";
import { Dialog } from "primereact/dialog";
import { InputTextarea } from "primereact/inputtextarea";
import { Button } from "primereact/button";

interface Country {
  name: string;
  code: string;
}

const Profile = () => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  const stompClient = useSelector((state: any) => state.socket.stompClient);
  const loggedUser = useSelector((state: any) => state.auth.user);
  const profileUser = useSelector((state: any) => state.user.user);
  const profileRes = useSelector((state: any) => state.user.res);
  const [dialogVisible, setDialogVisible] = useState(false);
  const [messageText, setMessageText] = useState("");

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

  const sendMessage = () => {
    if (messageText.trim() === "") return;

    const data = {
      userId: loggedUser.id,
      content: messageText,
    };

    stompClient.send(
      `/app/user/${profileUser.username}/sendMessage`,
      { Authorization: `Bearer ${localStorage.getItem("token")}` },
      JSON.stringify(data)
    );

    setMessageText("");
    setDialogVisible(false);
  };

  const friendRequestAction = async () => {
    if (profileUser.friendshipStatus !== "accepted") {
      if (profileUser.friendshipStatus === "pending") {
        return await dispatch(
          friendThunks.CancelFriendRequest(profileUser.id) as any
        );
      } else if (profileUser.friendshipStatus === "requested") {
        return await dispatch(
          friendThunks.AcceptFriendRequest(profileUser.id) as any
        );
      } else {
        return await dispatch(friendThunks.AddFriend(profileUser.id) as any);
      }
    } else {
      return await dispatch(friendThunks.RemoveFriend(profileUser.id) as any);
    }
  };

  return (
    <div className={classes.newprofile_container}>
      <Toast ref={toast} />
      <SideBar idx={1} />
      <Dialog
        header="Send your message"
        visible={dialogVisible}
        style={{ width: "600px" }}
        onHide={() => {
          if (!dialogVisible) return;
          setDialogVisible(false);
        }}
      >
        <InputTextarea
          autoResize
          value={messageText}
          onChange={(e: any) => setMessageText(e.target.value)}
          style={{ width: "100%", minHeight: "200px" }}
          maxLength={700}
          placeholder="Type your message here..."
        />
        <div
          className="send_message_btn"
          style={{ width: "100%", marginTop: "20px", textAlign: "right" }}
        >
          <Button
            label="Send message"
            style={{ color: "#fff" }}
            onClick={sendMessage}
          />
        </div>
      </Dialog>
      <div className={classes.userprofile}>
        <GroofyHeader />
        {profileRes.status === "success" ? (
          <>
            <div className={classes.up_info}>
              <div className={classes.left_up_info}>
                <div className={classes.up_info_img}>
                  {profileUser.photoUrl !== null ? (
                    <Image
                      src={profileUser.photoUrl}
                      alt="Image"
                      width="180"
                      preview
                    />
                  ) : (
                    <div
                      className={classes.account_color}
                      style={{ backgroundColor: profileUser.accountColor }}
                    >
                      <span>
                        {profileUser.username.charAt(0).toUpperCase()}
                      </span>
                    </div>
                  )}
                </div>
                <div className={classes.up_info_details}>
                  <div className={classes.up_info_d_box}>
                    <div className={classes.up_info_d_box_names}>
                      <h3>
                        {profileUser.displayName}
                        {
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
                        }
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
                          onClick={() => setDialogVisible(true)}
                        />
                        <button
                          className={` ${
                            profileUser.friendshipStatus === "pending"
                              ? `bi bi-person-fill-x ${classes.pending}`
                              : profileUser.friendshipStatus === "requested"
                              ? `bi bi-person-check-fill ${classes.requested}`
                              : profileUser.friendshipStatus === "accepted"
                              ? `bi bi-person-dash-fill ${classes.accepted}`
                              : "bi bi-person-plus-fill"
                          } ${classes.user_action_btn}`}
                          onClick={() => {
                            friendRequestAction()
                              .then((res: any) => {
                                toast.current?.show({
                                  severity: "success",
                                  summary: res.status,
                                  detail: res.message,
                                });
                              })
                              .catch((error: any) => {
                                toast.current?.show({
                                  severity: "error",
                                  summary: error.status,
                                  detail: error.message,
                                });
                              });
                          }}
                        />
                      </div>
                    )}
                  </div>
                  <div className={classes.psi_single_details}>
                    <span>
                      <img src="/Assets/SVG/calendar.svg" alt="calender" />
                      Joined
                      <span className={classes.beside}>
                        {FormatDate(profileUser.createdAt)}
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
                      <span>{profileUser.user_rating}</span>
                    </div>
                  </div>
                </div>
                <div className={classes.middle_down_section}>
                  <div className={classes.middle_down_info}>
                    <h3>World rank</h3>
                    <div className={classes.middle_down_box}>
                      <img src="/Assets/SVG/world_rank.svg" alt="RankImg" />
                      <span>
                        {profileUser.worldRank === 0
                          ? "Unranked"
                          : `#${profileUser.worldRank}`}
                      </span>
                    </div>
                  </div>
                  <div className={classes.middle_down_info}>
                    <h3>Max Rating</h3>
                    <div className={classes.middle_down_box}>
                      <img src="/Assets/Badges/Badge6.svg" alt="RankImg" />
                      <span>{profileUser.user_max_rating}</span>
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
                        <h3>
                          {profileUser.clanName === null
                            ? "No clan"
                            : profileUser.clanName}
                        </h3>
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
