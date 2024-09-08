import { clanActions } from "../slices/clan-slice";
import { reqInstance } from "../";
import { ClanRequestAction } from "../../shared/types";

const clearSearchedClans = () => {
  return async (dispatch: any) => {
    dispatch(clanActions.setClans({ clans: [], totalClans: 0 }));
  };
};

const getSearchedClans = (searchQuery: string, page: number, size: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/clans/search?name=${searchQuery}&p=${page}&s=${size}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(clanActions.setClans(response.data.body));
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
      }
    }
  };
};

const getClan = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      dispatch(clanActions.setIsLoading(true));
      try {
        const response = await reqInstance.get(`/clans/user`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(clanActions.setClan(response.data.body));
        dispatch(clanActions.setIsLoading(false));
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        dispatch(clanActions.setClan(null));
      }
    }
  };
};

const requestJoinClan = (clanId: string) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(clanActions.setIsLoading(true));
        const response = await reqInstance.post(
          `/clans/request/${clanId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(
          clanActions.setResponse({
            status: response.data.status,
            message: response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
      }
    }
  };
};

const createClan = (clanName: string) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(clanActions.setIsLoading(true));
        const response = await reqInstance.post(
          `/clans`,
          {
            name: clanName,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(
          clanActions.setResponse({
            status: response.data.status,
            message: response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        dispatch(clanActions.setClan(response.data.body));
        return response.data;
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        return error.response.data;
      }
    }
  };
};

const leaveClan = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(clanActions.setIsLoading(true));
        const response = await reqInstance.post(
          `/clans/leave`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(
          clanActions.setResponse({
            status: response.data.status,
            message: response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        return response.data;
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        return error.response.data;
      }
    }
  };
};

const clearClan = () => {
  return (dispatch: any) => {
    dispatch(clanActions.setClan(null));
  };
};

const clanRequest = (clanId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(clanActions.setIsLoading(true));
        const response = await reqInstance.post(
          `/clans/request/${clanId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(
          clanActions.setResponse({
            status: response.data.status,
            message: response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        dispatch(clanActions.updateClanRequest(clanId));
        return response.data;
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        return error.response.data;
      }
    }
  };
};

const getClanRequests = (page: number, size: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(clanActions.setIsLoading(true));
        const response = await reqInstance.get(
          `/clans/requests?p=${page}&s=${size}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(clanActions.setClanRequests(response.data.body));
        dispatch(clanActions.setIsLoading(false));
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
      }
    }
  };
};

const clanRequestAction = (cRA: ClanRequestAction) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        dispatch(clanActions.setIsLoading(true));
        const response = await reqInstance.post(
          `/clans/requests`,
          { ...cRA },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(
          clanActions.setResponse({
            status: response.data.status,
            message: response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        dispatch(clanActions.updateClanRequests(cRA.clanRequestId));
        return response.data;
      } catch (error: any) {
        dispatch(
          clanActions.setResponse({
            status: error.response.data.status,
            message: error.response.data.message,
          })
        );
        dispatch(clanActions.setIsLoading(false));
        return error.response.data;
      }
    }
  };
};

const clanThunks = {
  clearSearchedClans,
  getClan,
  getSearchedClans,
  requestJoinClan,
  createClan,
  leaveClan,
  clearClan,
  clanRequest,
  getClanRequests,
  clanRequestAction,
};

export default clanThunks;
