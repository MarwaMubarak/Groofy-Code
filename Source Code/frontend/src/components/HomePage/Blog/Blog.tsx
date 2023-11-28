import { useState } from "react";
import "./scss/blog.css";
import UserPopUp from "../../UserPopUP/UserPopUp";

const Blog = () => {
  const [likeActive, setLikeActive] = useState(false);
  const [currReact, setCurrReact] = useState("like");
  const [reactions, setReactions] = useState(false);
  return (
    <div className="blog-box">
      <div className="b-box-background">
        <div className="b-user-info">
          <div className="b-user-img">
            <img src="/Assets/Images/Hazem Adel.jpg" alt="ProfilePicture" />
          </div>
          <div className="b-user-name">
            <h3>Hazem Adel</h3>
            <span>3 hours ago.</span>
          </div>
          <UserPopUp
            username="Hazem Adel"
            userImg="/Assets/Images/Hazem Adel.jpg"
            status="In Game"
            gameType="Ranked Match"
            clanImg="/Assets/Images/clan1.png"
            clanName="Ghosts"
            rankImg="/Assets/Images/elite-rank.png"
            rankName="Elite"
            badges={[
              ["Groofy Predator", "/Assets/Images/apex-predator-rank.png"],
              ["High Accuracy", "/Assets/Images/attackbadge.png"],
              ["Master Wins", "/Assets/Images/win20badge.png"],
            ]}
          />
        </div>
        <div className="b-details">
          <h3> Exciting Milestone Unlocked: 20 Consecutive Wins!</h3>
          <p>
            Overcoming challenges, mastering strategies, and pushing my
            limits—today, I'm thrilled to share the achievement of winning 20
            games in a row! It's been an incredible journey filled with intense
            competition, lessons learned, and moments of pure adrenaline.
          </p>
          <p>
            This badge represents not just a string of victories but a testament
            to dedication, perseverance, and the support of an amazing
            community. Huge thanks to my teammates, opponents, and everyone who
            has been a part of this gaming adventure. Here's to the thrill of
            the game, the joy of victory, and the excitement of the next
            challenge! Ready for more epic gaming moments ahead. Let's keep the
            winning streak alive!
          </p>
          <div className="b-details-img">
            <img src="/Assets/Images/authPic.png" alt="ProfilePicture" />
          </div>
        </div>
        <div className="count-reactions">
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
          <span>3.2k</span>
        </div>
        <div className="reactions-section">
          <div
            className="react-button like"
            onClick={() => {
              setLikeActive((state) => !state);
              setCurrReact("like");
            }}
            onMouseEnter={() => setReactions(true)}
            onMouseLeave={() => setReactions(false)}
          >
            {likeActive === false ? (
              <>
                <img src="/Assets/SVG/Like white.svg" alt="Reaction"></img>
                <span style={{ color: "black" }}>Like</span>
              </>
            ) : currReact === "like" ? (
              <>
                <img src="/Assets/SVG/Like blue.svg" alt="Reaction"></img>
                <span style={{ color: "#00A9FF" }}> Like </span>
              </>
            ) : currReact === "love" ? (
              <>
                <img src="/Assets/SVG/Love Icon.svg" alt="Reaction" />
                <span style={{ color: "#f55353" }}> Love </span>
              </>
            ) : currReact === "haha" ? (
              <>
                <img src="/Assets/SVG/Laugh Icon.svg" alt="Reaction" />
                <span style={{ color: "#FEB139" }}> Haha </span>
              </>
            ) : (
              <>
                <img src="/Assets/SVG/Angry Icon.svg" alt="Reaction" />
                <span style={{ color: "#963232" }}> Angry </span>
              </>
            )}
          </div>
          <div
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
          </div>
          <div className="react-button">
            <img src="/Assets/SVG/comment.svg" alt="Comment"></img>
            <span>Comment</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Blog;
