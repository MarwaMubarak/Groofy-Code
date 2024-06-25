import { reqInstance } from "..";
import { matchActions } from "../slices/match-slice";
import { problemActions } from "../slices/problem-slice";

const getProblem = (user: any) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      let body = {
        player: {
          user_rating: user.currentTrophies,
          user_max_rating: user.maxRating,
          wins: user.wins,
          losses: user.losses,
          draws: user.draws,
          rate_800_cnt: user.rate_800_cnt,
          rate_900_cnt: user.rate_900_cnt,
          rate_1000_cnt: user.rate_1000_cnt,
          rate_1100_cnt: user.rate_1100_cnt,
          rate_1200_cnt: user.rate_1200_cnt,
          rate_1300_cnt: user.rate_1300_cnt,
          rate_1400_cnt: user.rate_1400_cnt,
          rate_1500_cnt: user.rate_1500_cnt,
          rate_1600_cnt: user.rate_1600_cnt,
          rate_1700_cnt: user.rate_1700_cnt,
          rate_1800_cnt: user.rate_1800_cnt,
          rate_1900_cnt: user.rate_1900_cnt,
          rate_2000_cnt: user.rate_2000_cnt,
          rate_2100_cnt: user.rate_2100_cnt,
          rate_2200_cnt: user.rate_2200_cnt,
          rate_2300_cnt: user.rate_2300_cnt,
          rate_2400_cnt: user.rate_2400_cnt,
          rate_2500_cnt: user.rate_2500_cnt,
          rate_2600_cnt: user.rate_2600_cnt,
          rate_2700_cnt: user.rate_2700_cnt,
          rate_2800_cnt: user.rate_2800_cnt,
          rate_2900_cnt: user.rate_2900_cnt,
          rate_3000_cnt: user.rate_3000_cnt,
          rate_3100_cnt: user.rate_3100_cnt,
          rate_3200_cnt: user.rate_3200_cnt,
          rate_3300_cnt: user.rate_3300_cnt,
          rate_3400_cnt: user.rate_3400_cnt,
          rate_3500_cnt: user.rate_3500_cnt,
        },
        solvedProblems: [
          {
            name: "",
            solvedCount: "",
            contestId: "",
            index: "",
            rating: "",
          },
        ],
      };
      dispatch(problemActions.setLoading(true));
      const response = await reqInstance.post(`/games/solo`, body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      dispatch(problemActions.setGameID(response.data.body.id));
      dispatch(problemActions.setProblem(response.data.body.problem));
      dispatch(problemActions.setProblemURL(response.data.body.problemURL));
      dispatch(problemActions.setLoading(false));
    }
  };
};

const problemThunks = { getProblem };

export default problemThunks;
