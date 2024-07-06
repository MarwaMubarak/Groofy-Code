import { InputText } from "primereact/inputtext";
import { GroofyHeader, GroofyWrapper, TeamCard } from "../../components";
import classes from "./scss/team.module.css";
import { useEffect, useRef, useState } from "react";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { useDispatch, useSelector } from "react-redux";
import { Toast } from "primereact/toast";
import { teamThunks } from "../../store/actions";

const Team = () => {
  const dispatch = useDispatch();
  const [createDialog, setCreateDialog] = useState<boolean>(false);
  const [teamName, setTeamName] = useState<string>("");
  const teams: any[] = useSelector((state: any) => state.team.teams);
  const isLoading: boolean = useSelector((state: any) => state.team.isLoading);
  const toast = useRef<Toast>(null);

  useEffect(() => {
    dispatch(teamThunks.GetUserTeams() as any);
  }, [dispatch]);

  const createTeam = async () => {
    if (teamName.trim() === "") {
      return { status: "failure", message: "Team name cannot be empty" };
    }
    return await dispatch(teamThunks.CreateTeam(teamName) as any);
  };

  return (
    <GroofyWrapper idx={4}>
      <Toast ref={toast} />
      <div className={classes.page_container}>
        <GroofyHeader />
        <Dialog
          header={"Create a team"}
          visible={createDialog}
          onHide={() => setCreateDialog(false)}
          className={classes.create_team_dialog}
        >
          <InputText
            placeholder="Team Name"
            value={teamName}
            onChange={(e) => setTeamName(e.target.value)}
            className={classes.input_text}
          />
          <Button
            label={`${isLoading ? "Creating..." : "Create"}`}
            className={classes.create_btn}
            onClick={() => {
              createTeam()
                .then((res: any) => {
                  if (res.status === "success") {
                    setCreateDialog(false);
                    setTeamName("");
                    toast.current?.show({
                      severity: "success",
                      summary: res.status,
                      detail: res.message,
                      life: 3000,
                    });
                  } else {
                    toast.current?.show({
                      severity: "error",
                      summary: res.status,
                      detail: res.message,
                      life: 3000,
                    });
                  }
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
        </Dialog>
        <div className={classes.teams_container}>
          <div className={classes.teams_container_header}>
            <h2 className={classes.header}>Your teams:</h2>
            <Button
              label="Create Team"
              icon="bi bi-plus-lg"
              className={classes.create_btn}
              onClick={() => setCreateDialog(true)}
            />
          </div>
          <div className={classes.teams_list}>
            {teams.length === 0 && (
              <div className={classes.no_teams}>No teams found</div>
            )}
            {teams.length > 0 &&
              teams.map((team: any, idx: number) => (
                <TeamCard
                  key={idx}
                  name={team.name}
                  membersCount={team.membersCount}
                  creatorUsername={team.creatorUsername}
                />
              ))}
          </div>
        </div>
      </div>
    </GroofyWrapper>
  );
};

export default Team;
