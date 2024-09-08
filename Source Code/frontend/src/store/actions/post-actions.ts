import { reqInstance } from "..";
import { postActions } from "../slices/post-slice";

const getPosts = (id: string) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        const response = await reqInstance.get(`/posts/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(postActions.setStatus(response.data.status));
        dispatch(postActions.setMessage(response.data.message));
        dispatch(postActions.setPosts(response.data.body));
      }
    } catch (error: any) {
      dispatch(postActions.setStatus(error.response.data.status));
      dispatch(postActions.setMessage(error.response.data.message));
    }
  };
};

const addPost = (content: string) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        const response = await reqInstance.post(
          "/posts",
          {
            content,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(postActions.setStatus(response.data.status));
        dispatch(postActions.setMessage(response.data.message));
        dispatch(postActions.addPost(response.data.body));
      }
    } catch (error: any) {
      dispatch(postActions.setStatus(error.response.data.status));
      dispatch(postActions.setMessage(error.response.data.message));
    }
  };
};

const updatePost = (postID: string, content: string) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        const response = await reqInstance.put(
          `/posts/${postID}`,
          {
            content,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        dispatch(postActions.updatePost(response.data.body));
      }
    } catch (error: any) {
      dispatch(postActions.setStatus(error.response.data.status));
      dispatch(postActions.setMessage(error.response.data.message));
    }
  };
};

const deletePost = (postID: string) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        const response = await reqInstance.delete(`/posts/${postID}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        dispatch(postActions.deletePost(postID));
        dispatch(postActions.setStatus(response.data.status));
        dispatch(postActions.setMessage(response.data.message));
      }
    } catch (error: any) {
      dispatch(postActions.setStatus(error.response.data.status));
      dispatch(postActions.setMessage(error.response.data.message));
    }
  };
};

const likePost = (postID: string, userID: string) => {
  return async (dispatch: any) => {
    const token = localStorage.getItem("token");
    try {
      if (token) {
        await reqInstance.post(
          `/posts/${postID}/like`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        const time = Date.now();
        dispatch(postActions.likePost({ userID, postID, time }));
        dispatch(postActions.setStatus(""));
        dispatch(postActions.setMessage(""));
      }
    } catch (error: any) {
      dispatch(postActions.setStatus(error.response.data.status));
      dispatch(postActions.setMessage(error.response.data.message));
    }
  };
};

const postThunks = { getPosts, addPost, deletePost, updatePost, likePost };

export default postThunks;
