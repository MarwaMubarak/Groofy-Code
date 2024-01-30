import { reqInstance } from "..";
import { postActions } from "../slices/post-slice";

const getPosts = (_id: string) => {
  return async (dispatch: any) => {
    try {
      const user = JSON.parse(localStorage.getItem("user")!);
      if (user.token) {
        reqInstance.defaults.headers.common[
          "authorization"
        ] = `Bearer ${user.token}`;
        const response = await reqInstance.get(`/posts/${_id}`);
        console.log(response.data.body);
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
      const user = JSON.parse(localStorage.getItem("user")!);
      if (user.token) {
        reqInstance.defaults.headers.common[
          "authorization"
        ] = `Bearer ${user.token}`;
        const response = await reqInstance.post("/post/create", {
          content,
        });
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
      const user = JSON.parse(localStorage.getItem("user")!);
      if (user.token) {
        reqInstance.defaults.headers.common[
          "authorization"
        ] = `Bearer ${user.token}`;
        const response = await reqInstance.put(`/post/update/${postID}`, {
          content,
        });
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
      const user = JSON.parse(localStorage.getItem("user")!);
      if (user.token) {
        reqInstance.defaults.headers.common[
          "authorization"
        ] = `Bearer ${user.token}`;
        const response = await reqInstance.delete(`/post/delete/${postID}`);
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

const likePost = (postID: string) => {
  return async (dispatch: any) => {
    try {
      const user = JSON.parse(localStorage.getItem("user")!);
      if (user.token) {
        const response = await reqInstance.post(
          `/post/addLike/${postID}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${user.token}`,
            },
          }
        );
        console.log("Response", response);
        dispatch(postActions.likePost({ userID: user._id, postID }));
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
