import { reqInstance } from "..";
import { SubmissionProps } from "../../shared/types";
import { gameActions } from "../slices/game-slice";
import { submissionActions } from "../slices/submission-slice";
import { authActions } from "../slices/auth-slice";
import { toastActions } from "../slices/toast-slice";
import { friendActions } from "../slices/friend-slice";

const getAllUserGames = (page: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/game?p=${page}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log("GAMES", response.data.body);
        dispatch(gameActions.setGames(response.data.body));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const getCurrentGame = (gameId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(gameActions.setLoading(true));
        const response = await reqInstance.get(`/game/${gameId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(gameActions.setGame(response.data.body));
        dispatch(gameActions.setLoading(false));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
        dispatch(gameActions.setLoading(false));
      }
    }
  };
};

const getInvitationPlayers = (invitationId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/game/invitation/${invitationId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(gameActions.setGamePlayers(response.data.body));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const createSoloGame = () => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        dispatch(gameActions.setLoading(true));
        const response = await reqInstance.post(
          "/game/solo",
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(gameActions.setGame(response.data.body));
        dispatch(gameActions.setLoading(false));
        dispatch(authActions.setUserGameId(response.data.body.id));
        return response.data;
      }
    } catch (error: any) {
      dispatch(gameActions.setResponse(error.response.data));
      dispatch(gameActions.setLoading(false));
      throw error.response.data;
    }
  };
};

const inviteToFriendlyGame = (friendId: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/game/beat-friend/invite/${friendId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(
          friendActions.changeInviteState({ friendId, isInvited: true })
        );
        return response.data;
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
        return error.response.data;
      }
    }
  };
};

const cancelInvitationToFriendlyGame = (friendId: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/game/beat-friend/cancelInvitation`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(
          friendActions.changeInviteState({ friendId, isInvited: false })
        );
        return response.data;
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
        return error.response.data;
      }
    }
  };
};

const acceptFriendlyGameInvitation = (invitationId: any, friendId: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/game/beat-friend/acceptInvitation?invitationId=${invitationId}`,
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
        dispatch(
          friendActions.changeInviteState({ friendId, isInvited: false })
        );
        return response.data;
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
        dispatch(gameActions.setWaitingPopup(false));
        dispatch(authActions.setExistingInvitation(null));
        throw error.response.data;
      }
    }
  };
};

const rejectFriendlyGameInvitation = (invitationId: any, friendId: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/game/beat-friend/rejectInvitation?invitationId=${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        dispatch(gameActions.setWaitingPopup(false));
        dispatch(
          friendActions.changeInviteState({ friendId, isInvited: false })
        );
        return response.data;
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
        dispatch(gameActions.setWaitingPopup(false));
        throw error.response.data;
      }
    }
  };
};

const createFriendlyGame = (invitationId: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/game/friend-match/${invitationId}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (response.data.body !== null) {
          dispatch(gameActions.setGame(response.data.body));
          dispatch(authActions.setUserGameId(response.data.body.id));
          dispatch(gameActions.setWaitingPopup(false));
          dispatch(authActions.setExistingInvitation(null));
          return {
            status: response.data.status,
            message: response.data.message,
            gameId: response.data.body.id,
          };
        }
        return response.data;
      } catch (error: any) {
        return dispatch(gameActions.setResponse(error.response.data)).payload;
      }
    }
  };
};

const setGameType = (gameType: string) => {
  return (dispatch: any) => {
    dispatch(gameActions.setGameType(gameType));
  };
};

const createRankedGame = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post("/game/ranked", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(gameActions.setGameType("Ranked"));
        if (response.data.body != null) {
          dispatch(gameActions.setGame(response.data.body));
          dispatch(authActions.setUserGameId(response.data.body.id));
          return {
            status: response.data.status,
            message: response.data.message,
            gameId: response.data.body.id,
          };
        }
        return response.data;
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const createCasualGame = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post("/game/casual", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(gameActions.setGameType("Casual"));
        if (response.data.body != null) {
          dispatch(gameActions.setGame(response.data.body));
          dispatch(authActions.setUserGameId(response.data.body.id));
          return {
            status: response.data.status,
            message: response.data.message,
            gameId: response.data.body.id,
          };
        }
        return response.data;
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const createVelocityGame = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post("/game/velocity", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(gameActions.setGameType("Velocity"));
        if (response.data.body != null) {
          dispatch(gameActions.setGame(response.data.body));
          dispatch(authActions.setUserGameId(response.data.body.id));
          return {
            status: response.data.status,
            message: response.data.message,
            gameId: response.data.body.id,
          };
        }
        return response.data;
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const updateGroofyGame = (gameId: number) => {
  return (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      dispatch(authActions.setUserGameId(gameId));
    }
  };
};

const submitCode = (submission: SubmissionProps) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(submissionActions.setIsLoading(true));
        const response = await reqInstance.post(
          "/game/" + submission.gameID + "/submit",
          { ...submission },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(submissionActions.setIsLoading(false));
        dispatch(submissionActions.setSubmission(response.data));
        await dispatch(getGameSubmissions(submission.gameID) as any);
        return response.data;
      } catch (error: any) {
        dispatch(submissionActions.setSubmission(error.response.data));
        dispatch(submissionActions.setIsLoading(false));
        throw error.response.data;
      }
    }
  };
};

const getGameSubmissions = (gameId: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/game/${gameId}/submissions`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(submissionActions.setSubmissions(response.data.body));
      } catch (error: any) {
        console.log("error", error.response.data);
      }
    }
  };
};

const changeSubmitState = (state: string) => {
  return (dispatch: any) => {
    dispatch(submissionActions.setSubmitState(state));
  };
};

const leaveGame = (gameId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.put(`/game/${gameId}/leave`, null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(authActions.setUserGameId(null));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const checkQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get("/game/user-queue", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.data.message === "NO") {
          dispatch(gameActions.setInQueue(false));
        } else {
          dispatch(gameActions.setInQueue(true));
          dispatch(gameActions.setGameType(response.data.message));
        }
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const checkRankedQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get("/game/user-queue/ranked", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(gameActions.setInQueue(response.data.message === "YES"));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const checkCasualQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get("/game/user-queue/casual", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(gameActions.setInQueue(response.data.message === "YES"));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const checkVelocityQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get("/game/user-queue/velocity", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(gameActions.setInQueue(response.data.message === "YES"));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const leaveQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        await reqInstance.post("/game/leaveQueue", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(toastActions.setToastShow(false));
        dispatch(gameActions.setInQueue(false));
        dispatch(gameActions.setGameType(null));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const leaveRankedQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        await reqInstance.post("/game/leaveRankedQueue", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(toastActions.setToastShow(false));
        dispatch(gameActions.setInQueue(false));
        dispatch(gameActions.setGameType(null));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const leaveCasualQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        await reqInstance.post("/game/leaveCasualQueue", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(toastActions.setToastShow(false));
        dispatch(gameActions.setInQueue(false));
        dispatch(gameActions.setGameType(null));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const leaveVelocityQueue = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        await reqInstance.post("/game/  ", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(toastActions.setToastShow(false));
        dispatch(gameActions.setInQueue(false));
        dispatch(gameActions.setGameType(null));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
      }
    }
  };
};

const dismissToast = () => {
  return (dispatch: any) => {
    dispatch(toastActions.setToastShow(false));
    dispatch(gameActions.setInQueue(false));
  };
};

const changeWaitingPopup = (state: boolean) => {
  return (dispatch: any) => {
    dispatch(gameActions.setWaitingPopup(state));
  };
};

const changeSearchFriendDialog = (state: boolean) => {
  return (dispatch: any) => {
    dispatch(gameActions.setFriendlyDialog(state));
  };
};

const gameNotify = (
  waitingPopUpState: boolean,
  searchFriendDialogState: boolean,
  invitationId: any
) => {
  return (dispatch: any) => {
    dispatch(gameActions.setWaitingPopup(waitingPopUpState));
    dispatch(gameActions.setFriendlyDialog(searchFriendDialogState));
    dispatch(authActions.setExistingInvitation(invitationId));
  };
};

const gameThunks = {
  getAllUserGames,
  getCurrentGame,
  createSoloGame,
  createRankedGame,
  createCasualGame,
  createVelocityGame,
  updateGroofyGame,
  submitCode,
  leaveGame,
  checkQueue,
  checkRankedQueue,
  checkCasualQueue,
  checkVelocityQueue,
  leaveRankedQueue,
  leaveCasualQueue,
  leaveVelocityQueue,
  dismissToast,
  changeSubmitState,
  getGameSubmissions,
  inviteToFriendlyGame,
  createFriendlyGame,
  cancelInvitationToFriendlyGame,
  acceptFriendlyGameInvitation,
  rejectFriendlyGameInvitation,
  changeWaitingPopup,
  getInvitationPlayers,
  gameNotify,
  changeSearchFriendDialog,
  setGameType,
  leaveQueue,
};

export default gameThunks;
