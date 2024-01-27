import { ChangeEvent, useEffect, useState } from "react";
import "./scss/singlepost.css";
import { SinglePostProps } from "../../../shared/types";
import { useDispatch } from "react-redux";
import postThunks from "../../../store/actions/post-actions";
import useFormatDate from "../../../shared/hooks/formatDate";

const SinglePost = (props: SinglePostProps) => {
  const [likeActive, setLikeActive] = useState(false);
  const _id = localStorage.getItem("_id") as string;
  const dispatch = useDispatch();
  const [isEdit, setIsEdit] = useState(false);
  const [editContent, setEditContent] = useState(props.postContent);
  const [time, setTime] = useState(useFormatDate(props.postTime));

  const FormatDate = (curDate: string) => {
    const currentDate = new Date();
    const date = new Date(curDate);

    const diffTime = Math.abs(currentDate.getTime() - date.getTime());
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
    const diffHours = Math.floor(diffTime / (1000 * 60 * 60));
    const diffMinutes = Math.floor(diffTime / (1000 * 60));
    const diffSeconds = Math.floor(diffTime / 1000);

    if (diffSeconds < 60) return `${diffSeconds} seconds ago`;
    if (diffMinutes < 60) return `${diffMinutes} minutes ago`;
    if (diffHours < 24) return `${diffHours} hours ago`;

    if (diffDays == 0) {
      return `Today`;
    }
    if (diffDays == 1) {
      return `Yesterday`;
    }
    if (diffDays < 7) {
      return `${diffDays} days ago`;
    }
    if (diffDays < 30) {
      return `${Math.floor(diffDays / 7)} weeks ago`;
    }
    if (diffDays < 365) {
      return `${Math.floor(diffDays / 30)} months ago`;
    }
    if (diffDays >= 365) {
      return `${Math.floor(diffDays / 365)} years ago`;
    }
  };

  useEffect(() => {
    const intervalId = setInterval(() => {
      setTime(FormatDate(props.postTime));
    }, 1000);

    // Cleanup function to clear the interval when component unmounts
    return () => {
      clearInterval(intervalId);
    };
  }, [props.postTime]);

  const handleDelete = (postID: string) => {
    // @ts-ignore
    const ret = dispatch(postThunks.deletePost(postID));
    if (ret instanceof Promise) {
      ret.then((res) => {
        let newPosts = props.posts.filter((post: any) => post._id !== postID);
        props.setPosts(newPosts);
        // props.posts = props.posts.filter((post: any) => post._id !== postID);
      });
    }
  };

  const handleUpdate = (postID: string, content: string) => {
    if (content.trim() === "") return;
    // @ts-ignore
    const ret = dispatch(postThunks.updatePost(postID, content));
    if (ret instanceof Promise) {
      ret.then((res) => {
        let newPosts: any[] = [];
        props.posts.forEach((post: any) => {
          const curPost = { ...post };
          if (post._id === postID) {
            curPost["content"] = content;
            const currentDate = new Date();
            curPost["updatedAt"] = currentDate.toISOString();
          }
          newPosts.push(curPost);
        });
        props.setPosts(newPosts);
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
          <span>{useFormatDate(props.postTime)}</span>
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
