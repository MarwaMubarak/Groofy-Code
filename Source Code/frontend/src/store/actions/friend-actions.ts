import { friendActions } from "../slices/friend-slice";
import { userActions } from "../slices/user-slice";
import { reqInstance } from "..";

const GetFriends = (page: number = 0, size: number = 10) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/friends/accepted?page=${page}&size=${size}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(friendActions.setFriends(response.data.body));
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const GetFriendRequests = (page: number = 0, size: number = 10) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/friends/pending?page=${page}&size=${size}}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(friendActions.setFriendRequests(response.data.body));
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const GetFriendsCount = (friendId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(
          `/friends/accepted_cnt/${friendId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(friendActions.setFriendsCnt(response.data.body));
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const GetFriendRequestsCount = () => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.get(`/friends/pending_cnt`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(friendActions.setRequestsCnt(response.data.body));
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const AddFriend = (friendId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.post(
          `/friends/send?friendId=${friendId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(userActions.setFriendshipStatus("pending"));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const AcceptFriendRequest = (friendId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.put(
          `/friends/accept?friendId=${friendId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(userActions.setFriendshipStatus("accepted"));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const RejectFriendRequest = (friendId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.delete(
          `/friends/reject?friendId=${friendId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(userActions.setFriendshipStatus(null));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const CancelFriendRequest = (friendId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.delete(
          `/friends/cancel?friendId=${friendId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(userActions.setFriendshipStatus(null));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const RemoveFriend = (friendId: number) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await reqInstance.delete(
          `/friends/delete?friendId=${friendId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(friendActions.removeFriend(friendId));
        return response.data;
      } catch (error: any) {
        throw error.response.data;
      }
    }
  };
};

const friendThunks = {
  GetFriends,
  GetFriendRequests,
  GetFriendsCount,
  GetFriendRequestsCount,
  AddFriend,
  AcceptFriendRequest,
  RejectFriendRequest,
  CancelFriendRequest,
  RemoveFriend,
};

export default friendThunks;
