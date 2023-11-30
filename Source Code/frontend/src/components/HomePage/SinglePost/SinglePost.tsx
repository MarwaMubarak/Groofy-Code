import { useState } from "react";
import "./scss/singlepost.css";
import { SinglePostProps } from "../../../shared/types";

const SinglePost = (props: SinglePostProps) => {
  const [likeActive, setLikeActive] = useState(false);
  return (
    <div className="single-post">
      <div className="single-post-info">
        <div className="single-post-info-div">
          <img
            className="s-p-img"
            src={props.postUserImg}
            alt="ProfilePicture"
          />
          <div className="s-p-details">
            <h3>{props.postUser}</h3>
            <p>{props.postContent}</p>
          </div>
        </div>
        <div className="single-post-controls">
          <span>3 hours ago</span>
          <div className="controls">
            <img src="/Assets/SVG/edit.svg" alt="Edit" />
            <img src="/Assets/SVG/delete.svg" alt="Delete" />
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
