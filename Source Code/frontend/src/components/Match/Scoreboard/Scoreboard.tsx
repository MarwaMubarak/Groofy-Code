import { useDispatch, useSelector } from "react-redux";
import classes from "./scss/scoreboard.module.css";
import ProfileImage from "../../ProfileImage/ProfileImage";
import { useEffect } from "react";
import { userThunks } from "../../../store/actions";

const Scoreboard = () => {
  const dispatch = useDispatch();
  const loggedUser = useSelector((state: any) => state.auth.user);
  const opponent = useSelector((state: any) => state.user.user);
  const gameType = useSelector((state: any) => state.game.gameType);
  const gameDuration = useSelector((state: any) => state.game.duration);
  const players1Ids: any[] = useSelector(
    (state: any) => state.game.players1Ids
  );
  const players2Ids: any[] = useSelector(
    (state: any) => state.game.players2Ids
  );

  console.log("Player1ids", players1Ids);

  console.log("Player2ids", players2Ids);

  useEffect(() => {
    const getUserById = async (id: any) => {
      await dispatch(userThunks.getUserById(id) as any);
    };
    if (players1Ids?.at(0) === loggedUser.id) {
      players2Ids?.forEach((id: any) => {
        getUserById(id);
      });
    } else {
      players1Ids?.forEach((id: any) => {
        getUserById(id);
      });
    }
  }, [dispatch, loggedUser.id, players1Ids, players2Ids]);

  console.log("Oppo", opponent);

  if (opponent === null && gameType !== "Solo") return <div>Loading...</div>;

  return (
    <div className={classes.scoreboard}>
      <div className={classes.m_infobox}>
        <div className={classes.dot}></div>
        <div
          className={classes.match_info}
        >{`${gameType} Game | ${gameDuration} Min`}</div>
      </div>
      <div className={classes.players}>
        {gameType !== "Solo" && (
          <>
            <div className={classes.player}>
              {/* <div className={classes.first_status}></div> */}
              <div className={classes.info}>
                <div className={classes.usn}>{loggedUser.username}</div>
                <div className={classes.img}>
                  <ProfileImage
                    photoUrl={loggedUser.photoUrl}
                    username={loggedUser.username}
                    style={{
                      cursor: "pointer",
                      backgroundColor: loggedUser.accountColor,
                      width: "45px",
                      height: "45px",
                      fontSize: "20px",
                    }}
                    canClick={false}
                  />
                </div>
              </div>
              {/* <div className={classes.pr_st + " " + classes.first}>
            <div className={classes.pr + " " + classes.y}>1</div>
            <div className={classes.pr + " " + classes.n}>2</div>
            <div className={classes.pr + " " + classes.p}>3</div>
          </div> */}
            </div>

            <span className={classes.vs_word}>VS</span>
            <div className={classes.player}>
              {/* <div className={classes.pr_st + " " + classes.second}>
            <div className={classes.pr + " " + classes.y}>1</div>
            <div className={classes.pr + " " + classes.n}>2</div>
            <div className={classes.pr + " " + classes.y}>3</div>
          </div> */}
              <div className={classes.info}>
                <div className={classes.usn}>{opponent?.username}</div>
                <div className={classes.img}>
                  <ProfileImage
                    photoUrl={opponent?.photoUrl}
                    username={opponent?.username}
                    style={{
                      cursor: "pointer",
                      backgroundColor: opponent?.accountColor,
                      width: "45px",
                      height: "45px",
                      fontSize: "20px",
                    }}
                    canClick={false}
                  />
                </div>
              </div>
              {/* <div className={classes.second_status}>
            <div className={classes.finished}>
              <img src="/Assets/Images/success.png" alt="Success" />
            </div>
          </div> */}
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default Scoreboard;
