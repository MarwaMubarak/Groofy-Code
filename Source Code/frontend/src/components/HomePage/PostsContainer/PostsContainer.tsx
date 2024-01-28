import { ChangeEvent, RefObject, useEffect, useState } from "react";
import { GBtn, Posts } from "../..";
import { useDispatch, useSelector } from "react-redux";
import { Toast } from "primereact/toast";
import postThunks from "../../../store/actions/post-actions";
// import classes from "./scss/posts-container.module.css";
import "./scss/posts-container.css";

const PostsContainer = ({ toast }: { toast: RefObject<Toast> }) => {
  const dispatch = useDispatch();
  const user = useSelector((state: any) => state.auth.user);
  const resStatus = useSelector((state: any) => state.post.status);
  const resMessage = useSelector((state: any) => state.post.message);
  const allPosts: any[] = useSelector((state: any) => state.post.body);
  const [newPostContent, setNewPostContent] = useState("");

  const handleExpanding = (e: ChangeEvent<HTMLTextAreaElement>) => {
    autoExpand(e.target);
    setNewPostContent(e.target.value);
  };
  const autoExpand = (textarea: HTMLTextAreaElement) => {
    textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
  };

  const postHandler = (event: any) => {
    event.preventDefault();
    const addNewPost = async () => {
      await dispatch(postThunks.addPost(newPostContent) as any);
    };
    if (newPostContent.trim() === "") {
      (toast.current as any)?.show({
        severity: "info",
        summary: "Info",
        detail: "The post is empty",
        life: 1500,
      });
      return;
    }
    addNewPost();
  };

  useEffect(() => {
    const fetchPosts = async () => {
      await dispatch(postThunks.getPosts(user._id) as any);
    };
    fetchPosts();
  }, [dispatch, user]);

  useEffect(() => {
    if (
      resStatus === "" ||
      resMessage === "" ||
      resMessage === "All posts returned."
    )
      return;
    if (resStatus === "success") {
      (toast.current as any)?.show({
        severity: "success",
        summary: "Success",
        detail: resMessage,
        life: 1500,
      });
      setNewPostContent("");
    } else {
      (toast.current as any)?.show({
        severity: "error",
        summary: "Failed",
        detail: resMessage,
        life: 1500,
      });
    }
  }, [allPosts.length, resMessage, resStatus, toast]);

  return (
    <form className="posts-container" onSubmit={postHandler}>
      <div className="post-box">
        <div className="post-row">
          <img src={user.photo.url} alt="UserPhoto" />
          <textarea
            value={newPostContent}
            placeholder="Share your coding insights and experiences"
            onChange={handleExpanding}
            maxLength={500}
          ></textarea>
          <GBtn
            btnText="Quick Post"
            icnSrc="/Assets/SVG/quick.svg"
            clickEvent={() => {}}
            btnType={true}
          />
        </div>
        <Posts posts={allPosts} user={user} />
      </div>
    </form>
  );
};

export default PostsContainer;
