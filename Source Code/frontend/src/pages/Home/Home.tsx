import { useEffect, useRef, useState } from "react";
import { Toast } from "primereact/toast";
import {
  GroofyHeader,
  SideBar,
  Gamemode,
  PostsContainer,
  Friend,
  SearchedFriend,
} from "../../components";
import { postActions } from "../../store/slices/post-slice";
import { friendThunks, gameThunks, toastThunks } from "../../store/actions";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import classes from "./scss/home.module.css";
import { Toaster, toast as hotToast } from "react-hot-toast";
import { ConfirmDialog } from "primereact/confirmdialog";
import { Dialog } from "primereact/dialog";
import { InputTextarea } from "primereact/inputtextarea";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";

const Home = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state: any) => state.auth.user);
  const matchLoading = useSelector((state: any) => state.match.isLoading);
  const searchedFriends: any[] = useSelector(
    (state: any) => state.friend.friends
  );
  const toast = useRef<Toast>(null);
  dispatch(postActions.setStatus(""));
  dispatch(postActions.setMessage(""));
  const [dialogVisible, setDialogVisible] = useState(false);
  const [searchText, setSearchText] = useState("");

  console.log("Searched Friends: ", searchedFriends);

  const createSoloGame = async () => {
    return await dispatch(gameThunks.createSoloGame() as any);
  };

  const createRankedGame = async () => {
    return await dispatch(gameThunks.createRankedGame() as any);
  };

  const searchFriends = async () => {
    if (searchText === "") return;
    return await dispatch(friendThunks.SearchFriends(searchText) as any);
  };

  return (
    <div className={classes.home_container}>
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
      <Toaster />
      <SideBar idx={0} />
      <Dialog
        header="Send your message"
        visible={dialogVisible}
        style={{ width: "600px" }}
        onHide={() => {
          if (!dialogVisible) return;
          setDialogVisible(false);
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
          <div className={classes.friends}>
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
                />
              ))}
          </div>
        </div>
      </Dialog>
      <div className={classes.activity_section}>
        <GroofyHeader />
        <div className={classes.main_section}>
          <div className={classes.left_section}>
            <div className={classes.play_section_features}>
              <h3 className={classes.psf_header}>Play</h3>
              <div className={classes.play_container}>
                <Gamemode
                  id="velocity_code_play_card"
                  title="Velocity Code"
                  description="Face off in a 15-minute coding duel. Strategize, code swiftly, and emerge victorious in this high-stakes test of programming prowess."
                  img="/Assets/Images/clock.png"
                  clickEvent={() => {}}
                />
                <Gamemode
                  id="ranked_match_play_card"
                  title="Ranked Match"
                  description="Climb the coding ranks in intense head-to-head battles. Prove your skills, rise to the top, and become the coding champion."
                  img="/Assets/Images/ranked.png"
                  clickEvent={() => {
                    createRankedGame()
                      .then((res: any) => {
                        if (res.message === "Match started successfully") {
                          console.log("Game Response: ", res);
                          navigate(`/game/${res.gameId}`);
                        } else {
                          console.log("Waiting for opponent to join");
                          dispatch(toastThunks.changeToastShow(true) as any);
                        }
                      })
                      .catch((err: any) => {});
                  }}
                />
                <Gamemode
                  id="clan_play_card"
                  title="Join Clan"
                  description="Level up your gamplay and form alliances as you become a part of a gaming community by joining a clan."
                  img="/Assets/Images/clan.png"
                  clickEvent={() => {}}
                />
                <Gamemode
                  id="solo_practice_play_card"
                  title="Solo Practice"
                  description="Sharpen your skills and prepare for battle by practicing against a computer opponent."
                  img="/Assets/Images/lightbulb.png"
                  clickEvent={() => {
                    createSoloGame()
                      .then((res: any) => {
                        console.log("Game Response: ", res);
                        navigate(`/game/${res.body.id}`);
                      })
                      .catch((err: any) => {
                        toast.current?.show({
                          severity: "error",
                          summary: err.status,
                          detail: err.message,
                          life: 3000,
                        });
                      });
                  }}
                />
              </div>
            </div>
            <div className={classes.home_posts_container}>
              <h3 className={classes.hpc_title}>Posts</h3>
              <PostsContainer user={user} toast={toast} self={true} />
            </div>
          </div>
          <div className={classes.right_section}>
            <div className={classes.play_section_gamemode}>
              <h3 className={classes.psg_header}>Game Modes</h3>
              <div className={classes.gamemode_box}>
                <Gamemode
                  id="ranked_match_play_card"
                  title="Ranked Match"
                  img="/Assets/Images/ranked.png"
                  clickEvent={() => {
                    createRankedGame()
                      .then((res: any) => {
                        if (res.message === "Match started successfully") {
                          console.log("Game Response: ", res);
                          navigate(`/game/${res.gameId}`);
                        } else {
                          console.log("Waiting for opponent to join");
                          dispatch(toastThunks.changeToastShow(true) as any);
                        }
                      })
                      .catch((err: any) => {});
                  }}
                />
                <Gamemode
                  id="velocity_code_play_card"
                  title="Velocity Code"
                  img="/Assets/Images/clock.png"
                  clickEvent={() => {}}
                />
                <Gamemode
                  id="solo_practice_play_card"
                  title="Solo Practice"
                  img="/Assets/Images/lightbulb.png"
                  clickEvent={() => {
                    createSoloGame()
                      .then((res: any) => {
                        console.log("Game Response: ", res);
                        navigate(`/game/${res.body.id}`);
                      })
                      .catch((err: any) => {
                        toast.current?.show({
                          severity: "error",
                          summary: err.status,
                          detail: err.message,
                          life: 3000,
                        });
                      });
                  }}
                />
                <Gamemode
                  id="casual_match_card"
                  title="Casual Match"
                  img="/Assets/Images/battle.png"
                  clickEvent={() => {}}
                />
                <Gamemode
                  id="custom_match_card"
                  title="Custom Match"
                  img="/Assets/Images/customize.png"
                  clickEvent={() => {}}
                />
                <Gamemode
                  id="2v2_match_card"
                  title="2 Vs 2"
                  img="/Assets/Images/coop.png"
                  clickEvent={() => {}}
                />
                <Gamemode
                  id="clan_card"
                  title="Join Clan"
                  img="/Assets/Images/clan.png"
                  clickEvent={() => {}}
                />
                <Gamemode
                  id="beat_friend_card"
                  title="Beat a Friend"
                  img="/Assets/Images/friends.png"
                  clickEvent={() => setDialogVisible(true)}
                />
              </div>
            </div>

            <div className={classes.profile_section}>
              <h3 className={classes.ps_title}>Profile</h3>
              <div className={classes.ps_box}>
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
    </div>
  );
};

export default Home;
