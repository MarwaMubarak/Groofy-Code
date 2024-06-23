import { reqInstance } from "..";
import { problemActions } from "../slices/problem-slice";

const getProblem = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      dispatch(problemActions.setLoading(true));
      const response = await reqInstance.get(`/problemset/problem/1978/A`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      dispatch(problemActions.setProblem(response.data));
      dispatch(problemActions.setLoading(false));
    }
  };
};

const problemThunks = { getProblem };

export default problemThunks;
