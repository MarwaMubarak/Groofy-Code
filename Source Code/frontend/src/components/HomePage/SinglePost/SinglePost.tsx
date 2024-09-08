import { ChangeEvent, useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Toast } from "primereact/toast";
import { OverlayPanel } from "primereact/overlaypanel";
import { SinglePostProps } from "../../../shared/types";
import FormatDate from "../../../shared/functions/format-date";
import postThunks from "../../../store/actions/post-actions";
import "./scss/singlepost.css";
import ProfileImage from "../../ProfileImage/ProfileImage";

const SinglePost = (props: SinglePostProps) => {
  const dispatch = useDispatch();
  const post = useSelector((state: any) =>
    state.post.body.find((post: any) => post.id === props.postID)
  );
  const [likeActive, setLikeActive] = useState(
    post?.isLiked === 1 ? true : false
  );
  const [isEdit, setIsEdit] = useState(false);
  const [editContent, setEditContent] = useState(props.postContent);
  const [time, setTime] = useState(FormatDate(props.postTime));
  const [likesCnt, setLikesCnt] = useState(props.postLikesCnt);
  const canModify = post.postUserId === props.userid ? "can" : "not";
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
  }, [props.postTime, post]);

  useEffect(() => {
    setLikesCnt(post.likesCnt);
  }, [post.likesCnt]);

  const handleUpdate = (postID: string, content: string) => {
    const updatePost = async () => {
      await dispatch(postThunks.updatePost(postID, content) as any);
    };
    if (content.trim() === "") {
      (toast.current as any)?.show({
        severity: "info",
        summary: "Info",
        detail: "Post cannot be empty",
        life: 3000,
      });
      return;
    }
    try {
      updatePost();
      (toast.current as any)?.show({
        severity: "success",
        summary: "Success",
        detail: "Post updated succesfully",
        life: 3000,
      });
    } catch (error: any) {
      (toast.current as any)?.show({
        severity: "error",
        summary: "Failed",
        detail: "Error has occured",
        life: 3000,
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
        life: 3000,
      });
    } catch (error: any) {
      (toast.current as any)?.show({
        severity: "error",
        summary: "Failed",
        detail: "Error has occured",
        life: 3000,
      });
    }
  };

  const handleLike = (postID: string) => {
    const likePost = async () => {
      await dispatch(postThunks.likePost(postID, props.userid) as any);
    };
    likePost();
    setLikeActive(!likeActive);
  };

  return (
    <div className={`single-post ${isEdit} ${canModify}`}>
      <Toast ref={toast} />
      <div className="single-post-info">
        <div className="single-post-info-div">
          <ProfileImage
            photoUrl={props.postUserImg}
            username={props.postUser}
            style={{
              backgroundColor: props.postUserColor,
              width: "55px",
              height: "55px",
              marginRight: "10px",
              fontSize: "24px",
            }}
            canClick={false}
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
              <p>
                {props.postContent}
                {props.isEdited && (
                  <span className="edited_span">(Edited)</span>
                )}
              </p>
            )}
          </div>
        </div>
        <div className="single-post-controls">
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
        <span>{time}</span>
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
