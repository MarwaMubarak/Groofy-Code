import { authActions } from "../slices/auth-slice";
import { popupActions } from "../slices/popup-slice";

const setPopUpState = (show: boolean, body: any) => {
  return (dispatch: any) => {
    dispatch(popupActions.setContent({ show, body }));
    dispatch(authActions.setUserGameId(null));
  };
};

const popupThunks = { setPopUpState };

export default popupThunks;
