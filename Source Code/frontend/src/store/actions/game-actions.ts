import { reqInstance } from "..";
import { SubmissionProps } from "../../shared/types";
import { gameActions } from "../slices/game-slice";
import { submissionActions } from "../slices/submission-slice";
import { authActions } from "../slices/auth-slice";
import { toastActions } from "../slices/toast-slice";
import { get } from "http";

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
        console.log("Game: ", response.data.body);
        dispatch(gameActions.setGame(response.data.body));
        dispatch(gameActions.setLoading(false));
      } catch (error: any) {
        dispatch(gameActions.setResponse(error.response.data));
        dispatch(gameActions.setLoading(false));
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
        console.log("my response", response.data.body);
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
        console.log("PUSSSHHH RESPO", response.data);
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
        await reqInstance.post("/game/leaveRankedQueue", null, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(toastActions.setToastShow(false));
        dispatch(gameActions.setInQueue(false));
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

const gameThunks = {
  getAllUserGames,
  getCurrentGame,
  createSoloGame,
  createRankedGame,
  updateGroofyGame,
  submitCode,
  leaveGame,
  checkQueue,
  leaveQueue,
  dismissToast,
  changeSubmitState,
  getGameSubmissions,
};

export default gameThunks;
