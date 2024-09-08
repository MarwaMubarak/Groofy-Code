import { reqInstance } from "..";
import { authActions } from "../slices/auth-slice";
import { friendActions } from "../slices/friend-slice";
import { gameActions } from "../slices/game-slice";
import { teamActions } from "../slices/team-slice";

const GetUserTeams = (page: Number = 0) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(teamActions.setLoading(true));
        const response = await reqInstance.get(`/teams/user?page=${page}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(teamActions.setTeams(response.data.body));
        dispatch(teamActions.setLoading(false));
      } catch (error: any) {
        dispatch(teamActions.setLoading(false));
        throw error.response.data;
      }
    }
  };
};

const GetTeamByName = (teamName: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(teamActions.setLoading(true));
        const response = await reqInstance.get(`/teams/name/${teamName}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(teamActions.setSingleTeam(response.data.body));
        dispatch(teamActions.setLoading(false));
      } catch (error: any) {
        dispatch(teamActions.setLoading(false));
        dispatch(teamActions.setSingleTeam(null));
      }
    }
  };
};

const GetUserIsAdminTeams = (page: Number = 0) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/teams/my-teams?page=${page}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(teamActions.setTeams(response.data.body));
        dispatch(teamActions.setLoading(false));
        return response.data;
      } catch (error: any) {
        dispatch(teamActions.setLoading(false));
        throw error.response.data;
      }
    }
  };
};

const SearchUsersWithTeam = (searchText: string, teamId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/teams/search/to-invite?teamID=${teamId}&prefix=${searchText}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(friendActions.setFriends(response.data.body));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const CreateTeam = (teamName: string) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          "/teams/create",
          { name: teamName },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(teamActions.addTeam(response.data.body));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const SearchTeams = (searchText: string, teamId: number, page: Number = 0) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(teamActions.setLoading(true));
        const response = await reqInstance.get(
          `/teams/search/pagination?prefix=${searchText}&page=${page}&team1ID=${teamId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(teamActions.setTeams(response.data.body));
        dispatch(teamActions.setLoading(false));
      } catch (error: any) {
        dispatch(teamActions.setLoading(false));
        throw error.response.data;
      }
    }
  };
};

const InviteToTeam = (teamId: number, username: string) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/teams/invite?teamId=${teamId}&receiverUsername=${username}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const AcceptTeamInvitation = (invitationId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/teams/acceptInvitation?invitationId=${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const CancelTeamInvitation = (invitationId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/teams/cancelInvitation?invitationId=${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const RejectTeamInvitation = (invitationId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/teams/rejectInvitation?invitationId=${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const InviteTeamToGame = (teamId1: number, teamId2: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/api/match/invite/${teamId1}/${teamId2}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("INVITE TEAM TO GAME", response);
        // @TODO --> Change button state
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const AcceptTeamGameInvitation = (invitationId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/api/match/acceptInvitation/${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(gameActions.setWaitingPopup(true));
        dispatch(authActions.setExistingInvitation(invitationId));
        dispatch(gameActions.setFriendlyDialog(false));
        // dispatch(
        //   friendActions.changeInviteState({ friendId, isInvited: false })
        // );
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const CancelTeamGameInvitation = (invitationId: number) => {
  return async (disaptch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/api/match/cancelInvitation/${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const RejectTeamGameInvitation = (invitationId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/api/match/rejectInvitation/${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const DeleteTeam = (teamId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.delete(
          `/teams/delete?teamId=${teamId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(teamActions.removeTeam(teamId));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const ClearTeam = () => {
  return (dispatch: any) => {
    dispatch(teamActions.setSingleTeam(null));
  };
};

const ClearTeams = () => {
  return (dispatch: any) => {
    dispatch(teamActions.setTeams(null));
  };
};

const teamThunks = {
  GetUserTeams,
  GetTeamByName,
  SearchUsersWithTeam,
  CreateTeam,
  SearchTeams,
  InviteToTeam,
  AcceptTeamInvitation,
  CancelTeamInvitation,
  RejectTeamInvitation,
  InviteTeamToGame,
  AcceptTeamGameInvitation,
  CancelTeamGameInvitation,
  RejectTeamGameInvitation,
  DeleteTeam,
  ClearTeam,
  ClearTeams,
  GetUserIsAdminTeams,
};

export default teamThunks;
