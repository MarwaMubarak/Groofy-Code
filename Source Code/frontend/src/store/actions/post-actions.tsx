import { reqInstance } from "..";
import { postActions } from "../slices/post-slice";
// import { toast } from "react-toastify";

const getPosts = (_id: string) => {
  return async (dispatch: any) => {
    try {
      const response = await reqInstance.get(`/posts/${_id}`);
      console.log(response.data.body);
      dispatch(postActions.setPosts(response.data.body));
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
          content,
        });
        dispatch(postActions.addPost(response.data.body));
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
          content,
        });
        dispatch(postActions.updatePost(response.data.body));
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
        await reqInstance.delete(`/post/delete/${postID}`);
        dispatch(postActions.deletePost(postID));
      }
    } catch (error: any) {}
  };
};

const postThunks = { getPosts, addPost, deletePost, updatePost };

export default postThunks;
