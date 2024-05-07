import { useDispatch, useSelector } from "react-redux";
import { Chat, ClanRequests, GroofyWrapper } from "../../components";
import classes from "./scss/clan.module.css";
import { useEffect, useRef, useState } from "react";
import clanThunks from "../../store/actions/clan-actions";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";
import { Toast } from "primereact/toast";
import { useNavigate } from "react-router-dom";

const Clan = () => {
  const dispatch = useDispatch();
  const clan = useSelector((state: any) => state.clan.clan);
  const loggedUser = useSelector((state: any) => state.auth.user);
  const isLoading = useSelector((state: any) => state.clan.isLoading);
  const [fetchClan, setFetchClan] = useState(false);
  const [requestsVisible, setRequestsVisible] = useState(false);
  const toast = useRef<Toast>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const getClan = async () => {
      await dispatch(clanThunks.getClan() as any);
    };

    getClan();
  }, [dispatch, fetchClan]);

  console.log("User", loggedUser);

  const leaveClan = async () => {
    return await dispatch(clanThunks.leaveClan() as any);
  };

  const confirmLeaveClan = () => {
    confirmDialog({
      message: "Are you sure you want to leave the clan?",
      header: "Confirmation",
      icon: "pi pi-exclamation-triangle",
      accept: () =>
        leaveClan()
          .then((res: any) => {
            console.log("Response:", res);
            if (res.status === "success") {
              toast.current?.show({
                severity: "success",
                summary: res.status,
                detail: res.message,
                life: 3000,
              });
              setTimeout(() => {
                navigate("/");
              }, 1000);
            }
          })
          .catch((error: any) => {
            console.log("Error: ", error);
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
    <GroofyWrapper idx={3}>
      <Toast ref={toast} />
      <ConfirmDialog />
      <ClanRequests
        clanId={clan?.id}
        clanReqVisible={requestsVisible}
        setClanReqVisible={setRequestsVisible}
        fetchClan={fetchClan}
        setFetchClan={setFetchClan}
      />
      {isLoading && <div>Loading...</div>}
      {clan && (
        <div className={classes.clan_div}>
          <div className={classes.c_info}>
            <div className={classes.c_dashboard}>
              <div className={classes.c_details_wrapper}>
                <div className={classes.c_details}>
                  <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
                  <span>{clan.name}</span>
                </div>
                <div className={classes.c_actions}>
                  {loggedUser.leader && (
                    <i
                      className="bi bi-person-plus-fill"
                      onClick={() => setRequestsVisible(true)}
                    />
                  )}
                  <i
                    className="bi bi-box-arrow-left"
                    onClick={confirmLeaveClan}
                  />
                </div>
              </div>
              <div className={classes.c_stats}>
                <div className={classes.c_stat}>
                  <span className={classes.stat_title}>Members:</span>
                  <span className={classes.stat_num}>
                    {clan.membersCount}/10
                  </span>
                </div>
                <div className={classes.c_stat}>
                  <span className={classes.stat_title}>World Rank:</span>
                  <span className={classes.stat_num}>
                    {clan.worldRank === 0 ? "Unranked" : clan.worldRank}
                  </span>
                </div>
                <div className={classes.c_stat}>
                  <span className={classes.stat_title}>Total Matches:</span>
                  <span className={classes.stat_num}>{clan.totalMatches}</span>
                </div>
                <div className={classes.c_stat}>
                  <span className={classes.stat_title}>Wins:</span>
                  <span className={classes.stat_num}>{clan.wins}</span>
                </div>
                <div className={classes.c_stat}>
                  <span className={classes.stat_title}>Losses:</span>
                  <span className={classes.stat_num}>{clan.losses}</span>
                </div>
              </div>
            </div>
            <div className={classes.c_box}>
              <div className={classes.c_header}>
                <h3 className={classes.c_title}>Members</h3>
                <img
                  className={classes.ch_icn}
                  src="/Assets/SVG/view-all.svg"
                  alt="ViewAll"
                />
              </div>
              <div className={classes.m_box}>
                {clan.members.map((member: any, idx: number) => (
                  <div className={classes.member} key={idx}>
                    <div className={classes.m_info}>
                      <img src={member.photoUrl} alt="profilePhoto" />
                      <span className={classes.m_usn}>{member.username}</span>
                    </div>
                    <div
                      className={`${classes.m_rank} ${
                        member.role === "leader" ? classes.l : classes.m
                      }`}
                    >
                      {member.role}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
          <Chat type="clan" />
        </div>
      )}
    </GroofyWrapper>
  );
};

export default Clan;
