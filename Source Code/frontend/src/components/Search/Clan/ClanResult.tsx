import { Button } from "primereact/button";
import classes from "./scss/clanresult.module.css";
import { ClanResultProps } from "../../../shared/types";
import { useDispatch } from "react-redux";
import clanThunks from "../../../store/actions/clan-actions";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import { useNavigate } from "react-router-dom";

const ClanResult = (props: ClanResultProps) => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  const navigate = useNavigate();

  const clanReq = async () => {
    return await dispatch(clanThunks.clanRequest(props.clanId) as any);
  };

  return (
    <div className={classes.clan_card}>
      <Toast ref={toast} />
      <div className={classes.clan_info}>
        <div className={classes.clan_img}>
          <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
        </div>
        <div className={classes.clan_details}>
          <div className={classes.clan_name}>{props.clanName}</div>
          <div className={classes.clan_tags}>
            <span>Members: {props.members}/10</span>
            <span>World Rank: #{props.worldRank}</span>
            <span>Wins: {props.wins}</span>
            <span>Losses: {props.losses}</span>
          </div>
        </div>
      </div>
      <div className={classes.clan_actions}>
        {props.status === 0 && (
          <Button
            label="Cancel Request"
            icon="bi bi-x-lg"
            className={`${classes.cr_btn} ${classes.cancel}`}
            onClick={() => {
              clanReq()
                .then((res: any) => {
                  toast.current?.show({
                    severity: "success",
                    summary: res.status,
                    detail: res.message,
                    life: 3000,
                  });
                })
                .catch((error: any) => {
                  toast.current?.show({
                    severity: "error",
                    summary: error.status,
                    detail: error.message,
                    life: 3000,
                  });
                });
            }}
          />
        )}
        {props.status === 1 && (
          <Button
            label="Join"
            icon="bi bi-plus-lg"
            className={`${classes.cr_btn} ${classes.join}`}
            onClick={() => {
              clanReq()
                .then((res: any) => {
                  toast.current?.show({
                    severity: "success",
                    summary: res.status,
                    detail: res.message,
                    life: 3000,
                  });
                })
                .catch((error: any) => {
                  toast.current?.show({
                    severity: "error",
                    summary: error.status,
                    detail: error.message,
                    life: 3000,
                  });
                });
            }}
          />
        )}
        {props.status === 2 && (
          <Button
            label="View"
            icon="bi bi-eye-fill"
            className={`${classes.cr_btn} ${classes.view}`}
            onClick={() => navigate("/clan")}
          />
        )}
      </div>
    </div>
  );
};

export default ClanResult;
