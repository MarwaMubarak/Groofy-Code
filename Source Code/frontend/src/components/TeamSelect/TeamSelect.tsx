import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { Steps } from "primereact/steps";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { teamThunks } from "../../store/actions";
import { Button } from "primereact/button";
import classes from "./scss/team-select.module.css";
import { Toast } from "primereact/toast";
import { InputText } from "primereact/inputtext";

const TeamSelect = () => {
  const [activeIdx, setActiveIdx] = useState(0);
  const dispatch = useDispatch();
  const [selectedTeam, setSelectedTeam] = useState(null);
  const teams: any[] = useSelector((state: any) => state.team.teams);
  const [records, setRecords] = useState<any[]>([]);
  const toast = useRef<Toast>(null);
  const [searchText, setSearchText] = useState("");

  const items = [
    { label: "Choose your team" },
    { label: "Search for opponent teams" },
  ];

  useEffect(() => {
    const getTeams = async () => {
      await dispatch(teamThunks.GetUserIsAdminTeams() as any);
    };

    getTeams();
  }, [dispatch]);

  useEffect(() => {
    if (teams !== null && teams !== undefined && teams.length > 0) {
      const arr = Array.from(
        teams.map((team) => {
          return {
            id: team.id,
            name: team.name,
            member_1: team.members.at(0).username,
            member_2: team.members.at(1)?.username,
            member_3: team.members.at(2)?.username,
          };
        })
      );
      setRecords(arr);
    }
  }, [teams]);

  const searchTeams = async () => {
    if (searchText === "") return;
    return await dispatch(
      teamThunks.SearchTeams(searchText, (selectedTeam! as any).id) as any
    );
  };

  const isSelectable = (data: any) =>
    data.member_3 !== null &&
    data.member_3 !== undefined &&
    data.member_3 !== "";

  const isRowSelectable = (event: any) =>
    event.data ? isSelectable(event.data) : true;

  const sendInviteToTeam = async (teamId1: number, teamId2: number) => {
    return await dispatch(teamThunks.InviteTeamToGame(teamId1, teamId2) as any);
  };

  return (
    <div className={classes.team_select}>
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
      <Steps model={items} activeIndex={activeIdx} />

      {activeIdx === 0 ? (
        <DataTable
          value={records}
          selectionMode={"radiobutton"}
          selection={selectedTeam}
          onSelectionChange={(e: any) => setSelectedTeam(e.value)}
          dataKey="id"
          tableStyle={{ minWidth: "50rem" }}
          isDataSelectable={isRowSelectable}
        >
          <Column
            selectionMode="single"
            headerStyle={{ width: "3rem" }}
          ></Column>
          <Column field="name" header="Team Name"></Column>
          <Column field="member_1" header="Member 1"></Column>
          <Column field="member_2" header="Member 2"></Column>
          <Column field="member_3" header="Member 3"></Column>
        </DataTable>
      ) : (
        <>
          <div className={classes.team_select_header}>
            <InputText
              value={searchText}
              onChange={(e: any) => setSearchText(e.target.value)}
              placeholder="Search for teams..."
              className={classes.input_text}
            />
            <Button
              label="Search"
              style={{ color: "#fff" }}
              onClick={searchTeams}
              className={classes.search_btn}
            />
          </div>
          <div className={classes.team_select_content}>
            <div className={classes.friends}>
              {teams !== null &&
                teams.map((team: any, idx: number) => (
                  <div className={classes.single_team} key={idx}>
                    <h3>{team.name}</h3>
                    <Button
                      className={classes.invite_btn}
                      label="Invite team"
                      onClick={() => {
                        sendInviteToTeam((selectedTeam! as any).id, team.id)
                          .then((res: any) => {
                            if (res.status === "success") {
                              toast.current?.show({
                                severity: "success",
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
                  </div>
                ))}
            </div>
          </div>
        </>
      )}

      <div className={classes.control_buttons}>
        <Button
          label="Previous"
          disabled={activeIdx === 0}
          onClick={() => setActiveIdx(0)}
        />
        <Button
          label="Next"
          disabled={activeIdx === 1}
          onClick={() => {
            if (selectedTeam !== null) {
              dispatch(teamThunks.ClearTeams() as any);
              setActiveIdx(1);
            } else {
              toast.current?.show({
                severity: "error",
                summary: "Error",
                detail: "Please select a team",
                life: 2000,
              });
            }
          }}
        />
      </div>
    </div>
  );
};

export default TeamSelect;
