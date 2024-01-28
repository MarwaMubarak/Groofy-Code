import { ChangeEvent, useEffect, useRef, useState } from "react";
import "./scss/singlepost.css";
import { SinglePostProps } from "../../../shared/types";
import { useDispatch } from "react-redux";
import postThunks from "../../../store/actions/post-actions";
import FormatDate from "../../../shared/functions/format-date";
import { Toast } from "primereact/toast";
import { OverlayPanel } from "primereact/overlaypanel";
import { Button } from "primereact/button";

const SinglePost = (props: SinglePostProps) => {
  const dispatch = useDispatch();
  const [likeActive, setLikeActive] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [editContent, setEditContent] = useState(props.postContent);
  const [time, setTime] = useState(FormatDate(props.postTime));
  const toast = useRef<Toast>(null);
  const op = useRef<any>(null);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setTime(FormatDate(props.postTime));
    }, 1000);
    return () => {
      clearInterval(intervalId);
    };
  }, [props.postTime]);

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

  const handleExpanding = (e: ChangeEvent<HTMLTextAreaElement>) => {
    autoExpand(e.target);
    setEditContent(e.target.value);
  };
  const autoExpand = (textarea: HTMLTextAreaElement) => {
    textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
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
                {/* <Button
                  type="button"
                  icon="pi pi-image"
                  label="Image"
                  onClick={(e) => op.current?.toggle(e)}
                /> */}

                <img
                  src="/Assets/SVG/delete.svg"
                  alt="Delete"
                  // onClick={() => {
                  //   handleDelete(props.postID);
                  //   setEditContent(props.postContent);
                  // }}
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
      <div
        className="s-p-reactbtn"
        onClick={() => setLikeActive((state) => !state)}
      >
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
        <span className={`react-cnt ${likeActive}`}>3.2k</span>
      </div>
      {/* <div
          className={`reactions-popup ${reactions}`}
          onMouseEnter={() => setReactions(true)}
          onMouseLeave={() => setReactions(false)}
        >
          <div className="r-p-box">
            <div className="r-p-box-img">
              <img
                src="/Assets/SVG/reaction-like.svg"
                alt="Reaction"
                onClick={() => {
                  setLikeActive(true);
                  setCurrReact("like");
                  setReactions(false);
                }}
              />
            </div>
            <span>Like</span>
          </div>
          <div className="r-p-box">
            <div className="r-p-box-img">
              <img
                src="/Assets/SVG/reaction-love.svg"
                alt="Reaction"
                onClick={() => {
                  setLikeActive(true);
                  setCurrReact("love");
                  setReactions(false);
                }}
              />
            </div>
            <span>Love</span>
          </div>
          <div className="r-p-box">
            <div className="r-p-box-img">
              <img
                src="/Assets/SVG/reaction-laugh.svg"
                alt="Reaction"
                onClick={() => {
                  setLikeActive(true);
                  setCurrReact("haha");
                  setReactions(false);
                }}
              />
            </div>
            <span>Haha</span>
          </div>
          <div className="r-p-box">
            <div className="r-p-box-img">
              <img
                src="/Assets/SVG/reaction-angry.svg"
                alt="Reaction"
                onClick={() => {
                  setLikeActive(true);
                  setCurrReact("angry");
                  setReactions(false);
                }}
              />
            </div>
            <span>Angry</span>
          </div>
        </div> */}
      {/* <div className="s-p-reactions-cnt">
        <img
          className="single-reaction"
          src="/Assets/SVG/reaction-like.svg"
          alt="Reaction"
        />
        <img
          className="single-reaction"
          src="/Assets/SVG/reaction-love.svg"
          alt="Reaction"
        />
        <img
          className="single-reaction"
          src="/Assets/SVG/reaction-laugh.svg"
          alt="Reaction"
        />
        <img
        className="single-reaction"
        src="/Assets/SVG/reaction-angry.svg"
        alt="Reaction"
      />
      </div> */}
    </div>
  );
};

export default SinglePost;
