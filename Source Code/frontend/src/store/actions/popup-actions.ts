import { authActions } from "../slices/auth-slice";
import { gameActions } from "../slices/game-slice";
import { popupActions } from "../slices/popup-slice";

const setPopUpState = (show: boolean, body: any) => {
  return (dispatch: any) => {
    dispatch(popupActions.setContent({ show, body }));
    dispatch(authActions.setUserGameId(null));
    console.log("HEHRHEHEHEHRHEHEHEHEHEHEHEEHEHEHRHERHEHR");
    dispatch(authActions.setExistingInvitation(null));
    dispatch(gameActions.setWaitingPopup(false));
  };
};

const popupThunks = { setPopUpState };

export default popupThunks;
