import { createSlice } from "@reduxjs/toolkit";

const clanInitialState = {
  clan: null,
  clans: [],
  clanRequests: [],
  isLoading: false,
  totalClans: 0,
  totalClanRequests: 0,
  status: "",
  message: "",
};

const clanSlice = createSlice({
  initialState: clanInitialState,
  name: "clan",
  reducers: {
    setClan(state, action) {
      state.clan = action.payload;
    },
    setClans(state, action) {
      state.totalClans = action.payload.totalClans;
      state.clans = action.payload.clans;
    },
    setClanRequests(state, action) {
      state.totalClanRequests = action.payload.totalRequests;
      state.clanRequests = action.payload.requests;
    },
    updateClanRequest(state, action) {
      state.clans.forEach((clan: any) => {
        if (clan.id === action.payload) {
          if (clan.requestStatus === 0) {
            clan.requestStatus = 1;
          } else {
            clan.requestStatus = 0;
          }
        }
      });
    },
    updateClanRequests(state, action) {
      state.clanRequests = state.clanRequests.filter(
        (req: any) => req.id !== action.payload
      );
    },
    setIsLoading(state, action) {
      state.isLoading = action.payload;
    },
    setResponse(state, action) {
      state.status = action.payload.status;
      state.message = action.payload.message;
    },
  },
});

export const clanActions = clanSlice.actions;

export default clanSlice.reducer;
