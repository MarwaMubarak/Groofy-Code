import { useDispatch, useSelector } from "react-redux";
import classes from "./scss/match-header.module.css";
import { gameThunks } from "../../../store/actions";
import { useNavigate } from "react-router-dom";
import { Toast } from "primereact/toast";
import { useRef } from "react";
const MatchHeader = () => {
  const loggedUser = useSelector((state: any) => state.auth.user);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const toast = useRef<Toast>(null);

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
        <span>19:36</span> left
      </div>
      <div className={classes.user_area}>
        <div className={classes.info}>
          <span className={classes.h_usn}>{loggedUser.username}</span>
          <div className={classes.h_imgbox}>
            <img
              className={classes.pr_ph}
              src={loggedUser.photoUrl}
              alt="ProfilePhoto"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default MatchHeader;
