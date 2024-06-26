import { toastActions } from "../slices/toast-slice";

const changeToastShow = (show: boolean) => {
  return (dispatch: any) => {
    dispatch(toastActions.setToastShow(show));
  };
};

const toastThunks = { changeToastShow };

export default toastThunks;
