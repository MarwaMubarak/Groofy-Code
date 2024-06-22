import { socketActions } from "../slices/socket-slice";

const changeStompClient = (stompClient: any) => {
  return (dispatch: any) => {
    dispatch(socketActions.setStompClient(stompClient));
  };
};

const socketThunks = { changeStompClient };

export default socketThunks;
