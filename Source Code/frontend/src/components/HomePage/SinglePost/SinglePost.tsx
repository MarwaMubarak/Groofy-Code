import { ChangeEvent, useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Toast } from "primereact/toast";
import { OverlayPanel } from "primereact/overlaypanel";
import { SinglePostProps } from "../../../shared/types";
import FormatDate from "../../../shared/functions/format-date";
import postThunks from "../../../store/actions/post-actions";
import "./scss/singlepost.css";

const SinglePost = (props: SinglePostProps) => {
  const dispatch = useDispatch();
  const post = useSelector((state: any) =>
    state.post.body.find((post: any) => post._id === props.postID)
  );
  const [likeActive, setLikeActive] = useState(
    post?.likes.some((like: any) => like.user === props.userid)
  );
  const [isEdit, setIsEdit] = useState(false);
  const [editContent, setEditContent] = useState(props.postContent);
  const [time, setTime] = useState(FormatDate(props.postTime));
  const [likesCnt, setLikesCnt] = useState(props.postLikesCnt);
  console.log("Post", post);
  console.log("Likes Counter = ", likesCnt);
  const toast = useRef<Toast>(null);
  const op = useRef<any>(null);

  const handleExpanding = (e: ChangeEvent<HTMLTextAreaElement>) => {
    autoExpand(e.target);
    setEditContent(e.target.value);
  };
  const autoExpand = (textarea: HTMLTextAreaElement) => {
    textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
  };

  useEffect(() => {
    const intervalId = setInterval(() => {
      setTime(FormatDate(props.postTime));
    }, 1000);
    return () => {
      clearInterval(intervalId);
    };
  }, [props.postTime]);

  useEffect(() => {
    setLikesCnt(post.likes.length);
  }, [post.likes.length]);

  const handleUpdate = (postID: string, content: string) => {
    const updatePost = async () => {
      await dispatch(postThunks.updatePost(postID, content) as any);
    };
    if (content.trim() === "") {
      (toast.current as any)?.show({
        severity: "info",
        summary: "Info",
        detail: "Post cannot be empty",
        life: 1500,
      });
      return;
    }
    try {
      updatePost();
      (toast.current as any)?.show({
        severity: "success",
        summary: "Success",
        detail: "Post updated succesfully",
        life: 1500,
      });
    } catch (error: any) {
      (toast.current as any)?.show({
        severity: "error",
        summary: "Failed",
        detail: "Error has occured",
        life: 1500,
      });
    }
  };

  const handleDelete = (postID: string) => {
    const deletePost = async () => {
      await dispatch(postThunks.deletePost(postID) as any);
    };
    try {
      deletePost();
      (toast.current as any)?.show({
        severity: "success",
        summary: "Success",
        detail: "Post deleted succesfully",
        life: 1500,
      });
    } catch (error: any) {
      (toast.current as any)?.show({
        severity: "error",
        summary: "Failed",
        detail: "Error has occured",
        life: 1500,
      });
    }
  };

  const handleLike = (postID: string) => {
    const likePost = async () => {
      await dispatch(postThunks.likePost(postID) as any);
    };
    try {
      likePost().then(() => {
        setLikeActive(!likeActive);
      });
    } catch (error: any) {}
  };

  return (
    <div className={`single-post ${isEdit}`}>
      <Toast ref={toast} />
      <div className="single-post-info">
        <div className="single-post-info-div">
          <img
            className="s-p-img"
            src={props.postUserImg}
            alt="ProfilePicture"
          />
          <div className="s-p-details">
            <h3>{props.postUser}</h3>
            {isEdit ? (
              <textarea
                value={editContent}
                onChange={handleExpanding}
                maxLength={500}
              ></textarea>
            ) : (
              <p>{props.postContent}</p>
            )}
          </div>
        </div>
        <div className="single-post-controls">
          {props.isEdited && (
            <span style={{ marginRight: "10px" }}>(Edited)</span>
          )}
          <span>{time}</span>
          <div className="controls">
            {isEdit ? (
              <>
                <img
                  src="/Assets/Images/correctIcon.png"
                  alt="Accept"
                  onClick={() => {
                    handleUpdate(props.postID, editContent);
                    setIsEdit(false);
                    setEditContent(props.postContent);
                  }}
                />
                <img
                  src="/Assets/Images/wrongIcon.png"
                  alt="Cancel"
                  onClick={() => {
                    setIsEdit(false);
                    setEditContent(props.postContent);
                  }}
                />
              </>
            ) : (
              <>
                <img
                  src="/Assets/SVG/edit.svg"
                  alt="Edit"
                  onClick={() => {
                    setIsEdit(true);
                    setEditContent(props.postContent);
                  }}
                />
                <img
                  src="/Assets/SVG/delete.svg"
                  alt="Delete"
                  onClick={(event) => op.current.toggle(event)}
                />
              </>
            )}
            <OverlayPanel ref={op}>
              <div className="delete-panel">
                <span>Are you sure you want to delete this post?</span>
                <div className="delete-panel-btns">
                  <button
                    className="No-btn"
                    onClick={(event) => {
                      op.current.toggle(event);
                    }}
                  >
                    No
                  </button>
                  <button
                    className="Yes-btn"
                    onClick={() => {
                      handleDelete(props.postID);
                      setEditContent(props.postContent);
                    }}
                  >
                    Yes
                  </button>
                </div>
              </div>
            </OverlayPanel>
          </div>
        </div>
      </div>
      <div className="s-p-reactbtn" onClick={() => handleLike(props.postID)}>
        {likeActive === false ? (
          <>
            <img src="/Assets/SVG/Love Icon white.svg" alt="Reaction"></img>
          </>
        ) : (
          <>
            <img src="/Assets/SVG/Love Icon.svg" alt="Reaction"></img>
          </>
        )}
        <span className="react-info">Like</span>
        <span className={`react-cnt ${likeActive}`}>{likesCnt}</span>
      </div>
    </div>
  );
};

export default SinglePost;
