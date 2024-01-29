import { useEffect, useState } from "react";
import { GBtn, GroofyTA, Posts } from "..";
import { useDispatch, useSelector } from "react-redux";
import postThunks from "../../store/actions/post-actions";
import { useParams } from "react-router-dom";
import { PostsContainerProps } from "../../shared/types";
import classes from "./scss/posts-container.module.css";

const PostsContainer = ({ toast, self }: PostsContainerProps) => {
  const dispatch = useDispatch();
  const user = useSelector((state: any) => state.auth.user);
  const resStatus = useSelector((state: any) => state.post.status);
  const resMessage = useSelector((state: any) => state.post.message);
  const allPosts: any[] = useSelector((state: any) => state.post.body);
  const [newPostContent, setNewPostContent] = useState("");
  const { username: userProfile } = useParams();

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
    <form className={classes.posts_container} onSubmit={postHandler}>
      <div
        className={`${classes.post_box} ${
          self ? "" : userProfile !== user.username && classes.false
        }`}
      >
        {self && user && (
          <div className={classes.post_row}>
            <img src={user.photo.url} alt="UserPhoto" />
            <GroofyTA
              taValue={newPostContent}
              changeHandler={setNewPostContent}
            />
            <GBtn
              btnText="Quick Post"
              icnSrc="/Assets/SVG/quick.svg"
              clickEvent={() => {}}
              btnType={true}
            />
          </div>
        )}
        {!self && userProfile === user.username && (
          <div className={classes.post_row}>
            <img src={user.photo.url} alt="UserPhoto" />
            <GroofyTA
              taValue={newPostContent}
              changeHandler={setNewPostContent}
            />
            <GBtn
              btnText="Quick Post"
              icnSrc="/Assets/SVG/quick.svg"
              clickEvent={() => {}}
              btnType={true}
            />
          </div>
        )}
        <Posts posts={allPosts} user={user} />
      </div>
    </form>
  );
};

export default PostsContainer;
