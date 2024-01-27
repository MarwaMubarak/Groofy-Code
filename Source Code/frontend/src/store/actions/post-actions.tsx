import { reqInstance } from "..";
import { postActions } from "../slices/post-slice";
// import { toast } from "react-toastify";

const getPosts = (_id: string) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.get(`/posts/${_id}`);

      const dispatchResponse = await dispatch(
        postActions.setPost(response.data.body)
      );
      return dispatchResponse["payload"];
    } catch (error: any) {
      // toast.error(error.response.data.error);
    }
  };
};

const addPost = (content: string) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        reqInstance.defaults.headers.common[
          "authorization"
        ] = `Bearer ${token}`;
        const response = await reqInstance.post("/post/create", {
          content: content,
        });
        const dispatchResponse = await dispatch(
          postActions.setPost(response.data.body)
        );
        return dispatchResponse["payload"];
      }
    } catch (error: any) {}
  };
};

const deletePost = (postID: string) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        reqInstance.defaults.headers.common[
          "authorization"
        ] = `Bearer ${token}`;
        const response = await reqInstance.delete(`/post/delete/${postID}`);

        const dispatchResponse = await dispatch(
          postActions.setPost(response.data.body)
        );
        return dispatchResponse["payload"];
      }
    } catch (error: any) {}
  };
};

const updatePost = (postID: string, content: string) => {
  return async (dispatch: any) => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        reqInstance.defaults.headers.common[
          "authorization"
        ] = `Bearer ${token}`;
        const response = await reqInstance.put(`/post/update/${postID}`, {
          content: content,
        });
        const dispatchResponse = await dispatch(
          postActions.setPost(response.data.body)
        );
        return dispatchResponse;
      }
    } catch (error: any) {}
  };
};

const postThunks = { getPosts, addPost, deletePost, updatePost };

export default postThunks;
