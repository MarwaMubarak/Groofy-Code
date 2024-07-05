import { useDispatch, useSelector } from "react-redux";
import {
  Gamemode,
  GroofyHeader,
  SearchedFriend,
  SideBar,
  SimpleUser,
} from "../../components";
import classes from "./scss/play.module.css";
import { gameThunks, toastThunks, friendThunks } from "../../store/actions";
import { useNavigate } from "react-router-dom";
import { Toast } from "primereact/toast";
import { useRef, useState } from "react";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";

const Play = () => {
  const user = useSelector((state: any) => state.auth.user);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const toast = useRef<Toast>(null);
  const [searchText, setSearchText] = useState("");
  const searchedFriends: any[] = useSelector(
    (state: any) => state.friend.friends
  );
  const gamePlayers = useSelector((state: any) => state.game.gamePlayers);

  const waitingPopUp = useSelector((state: any) => state.game.waitingPopup);
  const searchFriendDialog = useSelector(
    (state: any) => state.game.searchFriendDialog
  );

  const createSoloGame = async () => {
    return await dispatch(gameThunks.createSoloGame() as any);
  };

  const createRankedGame = async () => {
    return await dispatch(gameThunks.createRankedGame() as any);
  };

  const createCasualGame = async () => {
    return await dispatch(gameThunks.createCasualGame() as any);
  };

  const createVelocityGame = async () => {
    return await dispatch(gameThunks.createVelocityGame() as any);
  };

  const createBeatAFriendGame = async () => {
    if (user.existingInvitationId === null) return;
    return await dispatch(
      gameThunks.createFriendlyGame(user.existingInvitationId) as any
    );
  };

  const closeWaitingPopUp = () => {
    dispatch(gameThunks.changeWaitingPopup(false) as any);
  };

  const openSearchFriendDialog = () => {
    dispatch(gameThunks.changeSearchFriendDialog(true) as any);
  };

  const closeSearchFriendDialog = () => {
    dispatch(gameThunks.changeSearchFriendDialog(false) as any);
  };

  const searchFriends = async () => {
    if (searchText === "") return;
    return await dispatch(friendThunks.SearchFriends(searchText) as any);
  };

  const getInvitationPlayers = async () => {
    return await dispatch(
      gameThunks.getInvitationPlayers(user.existingInvitationId) as any
    );
  };

  return (
    <div className={classes.play_page}>
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
      <SideBar idx={2} />
      <Dialog
        header="Friendly Match"
        visible={searchFriendDialog}
        style={{ width: "600px" }}
        onHide={() => {
          if (!searchFriendDialog) return;
          closeSearchFriendDialog();
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
      <Dialog
        header="Beat a friend Game"
        visible={waitingPopUp}
        style={{ width: "600px" }}
        onShow={getInvitationPlayers}
        onHide={() => {
          if (!waitingPopUp) return;
          closeWaitingPopUp();
        }}
        className={classes.beat_friend_waiting_dialog}
      >
        <div className={classes.beat_friend_waiting_wrapper}>
          <div className={classes.beat_friend_waiting_content}>
            <div className={`${classes.team_players} ${classes.first}`}>
              {gamePlayers.team1Players?.map((player: any, idx: number) => (
                <SimpleUser
                  key={idx}
                  username={`${player.username} (You)`}
                  accountColor={player.accountColor}
                  photoUrl={player.photoUrl}
                />
              ))}
            </div>
            <span className={classes.vs}>VS</span>
            <div className={`${classes.team_players} ${classes.second}`}>
              {gamePlayers.team2Players?.map((player: any, idx: number) => (
                <SimpleUser
                  key={idx}
                  username={player.username}
                  accountColor={player.accountColor}
                  photoUrl={player.photoUrl}
                  reverse={true}
                />
              ))}
            </div>
          </div>
          {gamePlayers.sender === true && (
            <div className={classes.start_game_btn}>
              <Button
                label="Start Game"
                style={{ color: "#fff" }}
                onClick={() => {
                  createBeatAFriendGame()
                    .then((res: any) => {
                      if (res.status === "success") {
                        navigate(`/game/${res.gameId}`);
                      }
                    })
                    .catch((error: any) => {});
                }}
              />
            </div>
          )}
        </div>
      </Dialog>
      <div className={classes.play_div}>
        <GroofyHeader />
        <div className={classes.play_mid_menu}>
          {/* <div className={classes.play_left_menu}>
            <div className={classes.play_img_user}>
              <img src="/Assets/Images/profile_picture.jpg" alt="UserPhoto" />
            </div>
            <div className={classes.play_versus_img}>
              <img src="/Assets/Images/versus.png" alt="Versus" />
            </div>
            <div className={classes.play_img_user}>
              <img src={user.photoUrl} alt="UserPhoto" />
            </div>
          </div> */}
          <div className={classes.play_menu}>
            <h3 className={classes.play_title}>Play a match</h3>
            <div className={classes.play_modes}>
              <Gamemode
                id="solo_practice_play_card"
                title="Solo Practice"
                description="Sharpen your skills and prepare for battle by practicing against a computer opponent."
                img="/Assets/Images/lightbulb.png"
                type="full"
                clickEvent={() => {
                  createSoloGame()
                    .then((res: any) => {
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
                title="Velocity Code"
                img="/Assets/Images/clock.png"
                description="Play a 15-min match and be a fast coder."
                type="full"
                clickEvent={() => {
                  createVelocityGame()
                    .then((res: any) => {
                      if (res.message === "Match started successfully") {
                        navigate(`/game/${res.gameId}`);
                      } else {
                        dispatch(toastThunks.changeToastShow(true) as any);
                      }
                    })
                    .catch((err: any) => {});
                }}
              />
              <Gamemode
                title="Casual Match"
                img="/Assets/Images/battle.png"
                description="Challenge others in a friendly competetion"
                type="full"
                clickEvent={() => {
                  createCasualGame()
                    .then((res: any) => {
                      if (res.message === "Match started successfully") {
                        navigate(`/game/${res.gameId}`);
                      } else {
                        dispatch(toastThunks.changeToastShow(true) as any);
                      }
                    })
                    .catch((err: any) => {});
                }}
              />
              <Gamemode
                title="Ranked Match"
                img="/Assets/Images/ranked.png"
                description="Climb up the ranks to be the master coder"
                type="full"
                clickEvent={() => {
                  createRankedGame()
                    .then((res: any) => {
                      if (res.message === "Match started successfully") {
                        navigate(`/game/${res.gameId}`);
                      } else {
                        dispatch(toastThunks.changeToastShow(true) as any);
                      }
                    })
                    .catch((err: any) => {});
                }}
              />
              <Gamemode
                title="Beat a Friend"
                img="/Assets/Images/friends.png"
                description="Compete against your friends to find the better coder"
                type="full"
                clickEvent={openSearchFriendDialog}
              />
              <Gamemode
                title="3 Vs 3"
                img="/Assets/Images/coop.png"
                description="Team up with your friend against others"
                type="full"
                clickEvent={() => {}}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Play;
