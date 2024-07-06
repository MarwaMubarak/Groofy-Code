import { Button } from "primereact/button";
import {
  GroofyHeader,
  GroofyWrapper,
  ProfileImage,
  SearchedFriend,
  SimpleUser,
} from "../../components";
import classes from "./scss/single-team.module.css";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useRef, useState } from "react";
import { friendThunks, teamThunks } from "../../store/actions";
import { Tooltip } from "primereact/tooltip";
import { Badge } from "primereact/badge";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";
import { confirmDialog, ConfirmDialog } from "primereact/confirmdialog";

const SingleTeam = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const loggedUser = useSelector((state: any) => state.auth.user);
  const team = useSelector((state: any) => state.team.singleTeam);
  const toast = useRef<Toast>(null);
  const [searchFriendDialog, setSearchFriendDialog] = useState(false);
  const searchedFriends: any[] = useSelector(
    (state: any) => state.friend.friends
  );
  const [searchText, setSearchText] = useState("");
  const { teamName } = useParams();

  useEffect(() => {
    const getTeam = async () => {
      await dispatch(teamThunks.GetTeamByName(teamName!) as any);
    };

    getTeam();
  }, [dispatch, teamName]);

  const searchFriends = async () => {
    if (searchText === "") return;
    return await dispatch(
      teamThunks.SearchUsersWithTeam(searchText, team.id) as any
    );
  };

  const deleteTeam = async () => {
    return await dispatch(teamThunks.DeleteTeam(team.id) as any);
  };

  const confirmDeleteTeam = () => {
    confirmDialog({
      message: "Are you sure you want to delete your team ?",
      header: "Confirmation",
      icon: "pi pi-exclamation-triangle",
      accept: () =>
        deleteTeam()
          .then((res: any) => {
            if (res.status === "success") {
              toast.current?.show({
                severity: "success",
                summary: res.status,
                detail: res.message,
                life: 3000,
              });

              setTimeout(() => {
                dispatch(teamThunks.ClearTeam() as any);
                navigate("/teams");
              }, 1500);
            }
          })
          .catch((error: any) => {
            toast.current?.show({
              severity: "error",
              summary: error.status,
              detail: error.message,
              life: 3000,
            });
          }),
      reject: () => null,
    });
  };

  return (
    <GroofyWrapper idx={10000}>
      <div className={classes.page_container}>
        <Toast ref={toast} style={{ padding: "0.75rem" }} />
        <ConfirmDialog />
        <GroofyHeader />
        <Dialog
          header="Search friends"
          visible={searchFriendDialog}
          style={{ width: "600px" }}
          onHide={() => {
            if (!searchFriendDialog) return;
            setSearchFriendDialog(false);
          }}
          className={classes.beat_friend_dialog}
        >
          <div className={classes.beat_friend_header}>
            <InputText
              value={searchText}
              onChange={(e: any) => setSearchText(e.target.value)}
              placeholder="Search for your friends..."
            />
            <Button
              label="Search"
              style={{ color: "#fff" }}
              onClick={searchFriends}
            />
          </div>
          <div className={classes.beat_friend_content}>
            <div className={classes.friends} style={{ marginTop: "20px" }}>
              {(searchedFriends === null || searchedFriends.length === 0) && (
                <h3>You have no friends</h3>
              )}
              {searchedFriends !== null &&
                searchedFriends.map((friend: any, idx: number) => (
                  <SearchedFriend
                    key={idx}
                    userId={friend.friendId}
                    username={friend.username}
                    photoUrl={friend.photoUrl}
                    accountColor={friend.accountColor}
                    isInvited={friend.isInvited}
                    teamId={team.id}
                    invitationId={friend.invitationId}
                  />
                ))}
            </div>
          </div>
        </Dialog>
        {team === null ? (
          <div> Team not found </div>
        ) : (
          <div className={classes.single_team_container}>
            <div className={classes.header}>
              <h3>Team Name: {team.name}</h3>
              {loggedUser.username === team.creatorUsername && (
                <div className={classes.buttonsBox}>
                  <Button
                    label="Delete team"
                    className={classes.create_btn}
                    onClick={confirmDeleteTeam}
                    style={{ backgroundColor: "#ff4d4f", color: "#fff" }}
                  />
                  <Button
                    label="Invite members"
                    className={classes.create_btn}
                    onClick={() => setSearchFriendDialog(true)}
                    style={{ backgroundColor: "#1890ff", color: "#fff" }}
                  />
                </div>
              )}
            </div>
            <div className={classes.members_box}>
              <h3>Members:</h3>
              <div className={classes.members}>
                {team.members?.map((member: any, idx: number) => (
                  <div className={classes.search_result} key={idx}>
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
                    <Link to={`/profile/${member.username}`}>
                      <div className={classes.sr_user}>
                        <ProfileImage
                          photoUrl={member.photoUrl}
                          username={member.username}
                          style={{
                            backgroundColor: member.accountColor,
                            width: "120px",
                            height: "120px",
                            fontSize: "60px",
                            marginRight: "10px",
                          }}
                          canClick={false}
                        />
                        <div className={classes.sr_username}>
                          <h3>{member.username}</h3>
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
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </GroofyWrapper>
  );
};

export default SingleTeam;
