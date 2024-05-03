import { reqInstance } from "..";
import { SubmissionProps } from "../../shared/types";
import { matchActions } from "../slices/match-slice";
import { submissionActions } from "../slices/submission-slice";
import { authActions } from "../slices/auth-slice";

const getAllUserMatches = (page: number, size: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/matches?p=${page}&s=${size}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(matchActions.setUserMatches(response.data));
      } catch (error: any) {
        dispatch(matchActions.setUserMatches(error.response.data));
      }
    }
  };
};

const getCurrentMatch = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(matchActions.setIsLoading(true));
        const response = await reqInstance.get("/matches/current", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(matchActions.setMatch(response.data));
        dispatch(matchActions.setIsLoading(false));
      } catch (error: any) {
        dispatch(matchActions.setMatch(error.response.data));
        dispatch(matchActions.setIsLoading(false));
      }
    }
  };
};

const createSoloMatch = () => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        dispatch(matchActions.setIsLoading(true));
        const response = await reqInstance.post(
          "/matches/solo-match",
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(matchActions.setMatch(response.data));
        dispatch(matchActions.setIsLoading(false));
        dispatch(authActions.setStatus(1));
        return response.data;
      }
    } catch (error: any) {
      dispatch(matchActions.setMatch(error.response.data));
      dispatch(matchActions.setIsLoading(false));
      throw error.response.data;
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
          "/submit-code",
          { ...submission },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(submissionActions.setIsLoading(false));
        dispatch(submissionActions.setSubmission(response.data));
        return response.data;
      } catch (error: any) {
        dispatch(submissionActions.setSubmission(error.response.data));
        dispatch(submissionActions.setIsLoading(false));
        throw error.response.data;
      }
    }
  };
};

const finishMatch = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          "/matches/finish-match",
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(matchActions.setMatch(response.data));
        dispatch(authActions.setStatus(0));
        return response.data;
      } catch (error: any) {
        dispatch(matchActions.setMatch(error.response.data));
        return error.response.data;
      }
    }
  };
};

const matchThunks = {
  getAllUserMatches,
  getCurrentMatch,
  createSoloMatch,
  submitCode,
  finishMatch,
};

export default matchThunks;
