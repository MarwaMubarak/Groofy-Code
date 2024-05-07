import { Dialog } from "primereact/dialog";
import { ClanRequestAction, ClanRequestsProps } from "../../../shared/types";
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useRef } from "react";
import clanThunks from "../../../store/actions/clan-actions";
import classes from "./scss/clan-requests.module.css";
import { Toast } from "primereact/toast";

const ClanRequests = (props: ClanRequestsProps) => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  const clanRequests: any[] = useSelector(
    (state: any) => state.clan.clanRequests
  );
  useEffect(() => {
    if (props.clanReqVisible) {
      const getClanRequests = async () => {
        await dispatch(clanThunks.getClanRequests(0, 10) as any);
      };
      getClanRequests();
    }
  }, [dispatch, props.clanReqVisible]);

  const requestAction = async (cRa: ClanRequestAction) => {
    return await dispatch(clanThunks.clanRequestAction(cRa) as any);
  };

  return (
    <Dialog
      header="Clan Requests"
      visible={props.clanReqVisible}
      style={{ width: "50vw" }}
      onHide={() => {
        props.setClanReqVisible(false);
        props.setFetchClan(!props.fetchClan);
      }}
    >
      <Toast ref={toast} />
      {clanRequests.length === 0 && <div>There is no requests.</div>}
      {clanRequests.length > 0 &&
        clanRequests.map((request: any, idx: number) => (
          <div key={idx} className={classes.single_request}>
            <div className={classes.user_info}>
              <img src={request.photoUrl} alt="UserPhoto" />
              <span>{request.username}</span>
            </div>
            <div className={classes.request_actions}>
              <button
                onClick={() => {
                  requestAction({
                    clanId: props.clanId,
                    clanRequestId: request.id,
                    action: "acc",
                  })
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
                        severity: "success",
                        summary: error.status,
                        detail: error.message,
                        life: 3000,
                      });
                    });
                }}
                className={`${classes.req_btn} ${classes.acc}`}
              >
                Accept
              </button>
              <button
                onClick={() => {
                  requestAction({
                    clanId: props.clanId,
                    clanRequestId: request.id,
                    action: "rej",
                  })
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
                        severity: "success",
                        summary: error.status,
                        detail: error.message,
                        life: 3000,
                      });
                    });
                }}
                className={`${classes.req_btn} ${classes.rej}`}
              >
                Reject
              </button>
            </div>
          </div>
        ))}
    </Dialog>
  );
};

export default ClanRequests;
