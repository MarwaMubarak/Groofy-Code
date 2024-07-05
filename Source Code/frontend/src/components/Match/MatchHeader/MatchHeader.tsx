import { useDispatch, useSelector } from "react-redux";
import classes from "./scss/match-header.module.css";
import { gameThunks } from "../../../store/actions";
import { useNavigate } from "react-router-dom";
import { Toast } from "primereact/toast";
import { useEffect, useRef, useState } from "react";
import { ProfileImage } from "../..";
const MatchHeader = () => {
  const loggedUser = useSelector((state: any) => state.auth.user);
  const gameEndTime = useSelector((state: any) => state.game.endTime);
  const [timeLeft, setTimeLeft] = useState("");
  // const [timeLeft, setTimeLeft] = useState(
  //   Date.now() - gameStartTime.getTime()
  // );
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const toast = useRef<Toast>(null);

  useEffect(() => {
    setInterval(() => {
      const now = Date.now();
      const timeLeft = Math.max(new Date(gameEndTime).getTime() - now, 0);

      const minutes = Math.floor(timeLeft / (1000 * 60));
      const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
      setTimeLeft(`${minutes}:${seconds < 10 ? "0" : ""}${seconds}`);
    }, 1000);
  }, [gameEndTime]);

  const leaveGame = async () => {
    return await dispatch(
      gameThunks.leaveGame(loggedUser.existingGameId) as any
    );
  };

  return (
    <div className={classes.header_div}>
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
      <div className={classes.logo_area}>
        <div className={classes.logo}>
          <img src="/Assets/Images/GroofyLogoCover.png" alt="Logo" />
        </div>
        <button
          className={classes.exit_btn}
          onClick={() => {
            leaveGame()
              .then((res: any) => {
                toast.current?.show({
                  severity: "success",
                  summary: res.status,
                  detail: res.message,
                  life: 3000,
                });
                setTimeout(() => {
                  navigate("/");
                }, 1200);
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
        >
          <img src="/Assets/SVG/exit.svg" alt="Exit" />
          <span>Leave</span>
        </button>
      </div>
      <div className={classes.m_dur}>
        <span>{timeLeft}</span> left
      </div>
      <div className={classes.user_area}>
        <div className={classes.info}>
          <span className={classes.h_usn}>{loggedUser.username}</span>
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
    </div>
  );
};

export default MatchHeader;
