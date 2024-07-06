import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  problemStatement: null,
  problemStatement2: null,
  problemStatement3: null,
  problemUrl: null,
  games: [],
  gameID: null,
  gameType: null,
  gameStatus: null,
  duration: null,
  startTime: null,
  endTime: null,
  isLoading: false,
  inQueue: false,
  players1Ids: [],
  players2Ids: [],
  problems1ID: [] as any,
  responseStatus: null,
  responseMessage: null,
  waitingPopup: null,
  searchFriendDialog: null,
  selectTeamDialog: null,
  gamePlayers: {},
  selectedProblem: null,
};

const gameSlice = createSlice({
  name: "game",
  initialState,
  reducers: {
    setGames(state, action) {
      state.games = action.payload;
    },
    setGame(state, action) {
      state.problemUrl = action.payload.problemUrl;
      state.gameID = action.payload.id;
      state.problemStatement = action.payload.problemStatement;
      state.problemStatement2 = action.payload.problemStatement2;
      state.problemStatement3 = action.payload.problemStatement3;
      state.duration = action.payload.duration;
      state.startTime = action.payload.startTime;
      state.endTime = action.payload.endTime;
      state.gameType = action.payload.gameType;
      state.gameStatus = action.payload.gameStatus;
      state.players1Ids = action.payload.players1Ids;
      state.players2Ids = action.payload.players2Ids;
      state.problems1ID = action.payload.problems1ID;
    },
    setResponse(state, action) {
      state.responseStatus = action.payload.status;
      state.responseMessage = action.payload.message;
    },
    setProblem(state, action) {
      state.problemStatement = action.payload;
    },
    setLoading(state, action) {
      state.isLoading = action.payload;
    },
    setProblemURL(state, action) {
      state.problemUrl = action.payload;
    },
    setGameID(state, action) {
      state.gameID = action.payload;
    },
    setInQueue(state, action) {
      state.inQueue = action.payload;
    },
    setWaitingPopup(state, action) {
      state.waitingPopup = action.payload;
    },
    setGamePlayers(state, action) {
      state.gamePlayers = action.payload;
    },
    setFriendlyDialog(state, action) {
      state.searchFriendDialog = action.payload;
    },
    setTeamDialog(state, action) {
      state.selectTeamDialog = action.payload;
    },
    setGameType(state, action) {
      state.gameType = action.payload;
    },
    setSelectedProblem(state, action) {
      state.selectedProblem = action.payload;
    },
  },
});

export const gameActions = gameSlice.actions;

export default gameSlice.reducer;
