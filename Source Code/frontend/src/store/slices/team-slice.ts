import { createSlice } from "@reduxjs/toolkit";

const teamInitialState = {
  teams: [] as any,
  singleTeam: {},
  isLoading: null,
};

const teamSlice = createSlice({
  name: "team",
  initialState: teamInitialState,
  reducers: {
    setTeams(state, action) {
      state.teams = action.payload;
    },
    setSingleTeam(state, action) {
      state.singleTeam = action.payload;
    },
    addTeam(state, action) {
      state.teams = [action.payload, ...state.teams];
    },
    removeTeam(state, action) {
      state.teams = state.teams.filter(
        (team: any) => team.id !== action.payload
      );
    },
    setLoading(state, action) {
      state.isLoading = action.payload;
    },
  },
});

export const teamActions = teamSlice.actions;

export default teamSlice.reducer;
