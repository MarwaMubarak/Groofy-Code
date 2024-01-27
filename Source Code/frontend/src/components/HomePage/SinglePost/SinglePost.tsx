import { ChangeEvent, useEffect, useState } from "react";
import "./scss/singlepost.css";
import { SinglePostProps } from "../../../shared/types";
import { useDispatch } from "react-redux";
import postThunks from "../../../store/actions/post-actions";
import FormatDate from "../../../shared/functions/format-date";

const SinglePost = (props: SinglePostProps) => {
  const dispatch = useDispatch();
  const [likeActive, setLikeActive] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [editContent, setEditContent] = useState(props.postContent);
  const [time, setTime] = useState(FormatDate(props.postTime));

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
    if (content.trim() === "") return;
    updatePost();
  };

  const handleDelete = (postID: string) => {
    const deletePost = async () => {
      await dispatch(postThunks.deletePost(postID) as any);
    };
    deletePost();
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
                  onClick={() => {
                    handleDelete(props.postID);
                    setEditContent(props.postContent);
                  }}
                />
              </>
            )}
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
