import {
  GBtn,
  GroofyHeader,
  SideBar,
  Gamemode,
  SinglePost,
} from "../../components";
import { ChangeEvent, useEffect, useState } from "react";
import "./scss/home.css";
import { useDispatch } from "react-redux";
import postThunks from "../../store/actions/post-actions";

const Home = () => {
  const [newPostContent, setNewPostContent] = useState("");
  const user = localStorage.getItem("user")
    ? JSON.parse(localStorage.getItem("user")!)
    : null;
  const dispatch = useDispatch();
  const [dummyposts, setDummyPosts] = useState<any[]>([]);

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
    const ret = dispatch(postThunks.addPost(newPostContent) as any);
    if (ret instanceof Promise) {
      ret.then((res: any) => {
        setDummyPosts([res, ...dummyposts]);
        setNewPostContent("");
      });
    }
  };

  useEffect(() => {
    const ret = dispatch(postThunks.getPosts(user["_id"]) as any);
    if (ret instanceof Promise) {
      ret.then((res: any) => {
        const tempPosts: any[] = [];
        res?.forEach((post: any) => {
          tempPosts.push(post);
        });
        setDummyPosts(tempPosts);
      });
    }
  }, []);

  return (
    <div className="home-container">
      <SideBar idx={0} />
      <div className="activity-section align">
        <GroofyHeader />
        <div className="play-section">
          <div className="play-section-features">
            <h3 className="p-s-f-header">Play</h3>
            <div className="play-container">
              <Gamemode
                title="Velocity Code"
                description="Face off in a 15-minute coding duel. Strategize, code swiftly, and emerge victorious in this high-stakes test of programming prowess."
                img="/Assets/Images/clock.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Ranked Match"
                description="Climb the coding ranks in intense head-to-head battles. Prove your skills, rise to the top, and become the coding champion."
                img="/Assets/Images/ranked.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Join Clan"
                description="Level up your gamplay and form alliances as you become a part of a gaming community by joining a clan."
                img="/Assets/Images/clan.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Solo Practice"
                description="Sharpen your skills and prepare for battle by practicing against a computer opponent."
                img="/Assets/Images/lightbulb.png"
                clickEvent={() => {}}
              />
            </div>
          </div>
          <div className="play-section-gamemode">
            <h3 className="p-s-g-header">Game Modes</h3>
            <div className="gamemode-box">
              <Gamemode
                title="Casual Match"
                img="/Assets/Images/battle.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Custom Match"
                img="/Assets/Images/customize.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="2 Vs 2"
                img="/Assets/Images/coop.png"
                clickEvent={() => {}}
              />
              <Gamemode
                title="Beat a Friend"
                img="/Assets/Images/friends.png"
                clickEvent={() => {}}
              />
            </div>
          </div>
        </div>
        <div className="media-section">
          <form className="posts-container">
            <h3 className="post-header">Posts</h3>
            <div className="post-box">
              <div className="post-row">
                <img src="/Assets/Images/Hazem Adel.jpg" alt="" />
                <textarea
                  value={newPostContent}
                  placeholder="Share your coding insights and experiences"
                  onChange={handleExpanding}
                  maxLength={500}
                ></textarea>
                <GBtn
                  btnText="Quick Post"
                  icnSrc="/Assets/SVG/quick.svg"
                  clickEvent={postHandler}
                />
              </div>
              <div className="posts">
                {Array.isArray(dummyposts) && dummyposts.length > 0
                  ? dummyposts.map((post, key) => (
                      <SinglePost
                        key={key}
                        postUser={user["username"]}
                        postUserImg="/Assets/Images/Hazem Adel.jpg"
                        postContent={post["content"]}
                        postTime={post["updatedAt"]}
                        postID={post["id"]}
                        posts={dummyposts}
                        setPosts={setDummyPosts}
                      />
                    ))
                  : null}

                {/* <SinglePost
                  postUser="Hazem Adel"
                  postUserImg="/Assets/Images/Hazem Adel.jpg"
                  postContent="Hello world! this is my first post"
                />
                <SinglePost
                  postUser="Hazem Adel"
                  postUserImg="/Assets/Images/Hazem Adel.jpg"
                  postContent="I wanna to beat someone now!!"
                />
                <SinglePost
                  postUser="Hazem Adel"
                  postUserImg="/Assets/Images/Hazem Adel.jpg"
                  postContent="Ez"
                />
                <SinglePost
                  postUser="Hazem Adel"
                  postUserImg="/Assets/Images/Hazem Adel.jpg"
                  postContent="Hakona Matata XD."
                /> */}
              </div>
            </div>
          </form>
          <div className="profile-section">
            <div className="ps-info">
              <div className="ps-header">
                <h3>Division</h3>
                <abbr title="Info">
                  <img
                    src="/Assets/SVG/info.svg"
                    className="info-btn"
                    alt="Info"
                  />
                </abbr>
              </div>
              <div className="ps-container">
                <div className="psi-box">
                  <img src="/Assets/Images/elite-rank.png" alt="RankImg" />
                  <div className="wrapper">
                    <span>Rank</span>
                    <h3>Elite</h3>
                  </div>
                </div>
                <div className="psi-box">
                  <img src="/Assets/Images/elite-rank.png" alt="ClanImg" />
                  <div className="wrapper">
                    <span>Clan</span>
                    <h3>Ghosts</h3>
                  </div>
                </div>
              </div>
            </div>
            <div className="ps-info">
              <div className="ps-header">
                <h3>Badges</h3>
                <abbr title="Info">
                  <img
                    src="/Assets/SVG/info.svg"
                    className="info-btn"
                    alt="Info"
                  />
                </abbr>
              </div>
              <div className="ps-container">
                <div className="psi-badge">
                  <img
                    src="/Assets/Images/apex-predator-rank.png"
                    alt="Badge"
                  />
                  <span>Groofy Predator</span>
                </div>
                <div className="psi-badge">
                  <img src="/Assets/Images/attackbadge.png" alt="Badge" />
                  <span>High Accuracy</span>
                </div>
                <div className="psi-badge">
                  <img src="/Assets/Images/win20badge.png" alt="Badge" />
                  <span>Master Wins</span>
                </div>
              </div>
            </div>
          </div>
          {/* <ProfileCard
            username="Hazem Adel"
            bio="Student at FCAI - Cairo University | ECPC’23 Champion - Candidate Master @Codeforces"
            worldRank={5}
            followers={5}
            level={5}
            percentage={30}
            userImg="/Assets/Images/Hazem Adel.jpg"
            clanImg="/Assets/Images/clan1.png"
            clanName="Ghosts"
            rankImg="/Assets/Images/elite-rank.png"
            rankName="Elite"
            badges={[
              ["Groofy Predator", "/Assets/Images/apex-predator-rank.png"],
              ["High Accuracy", "/Assets/Images/attackbadge.png"],
              ["Master Wins", "/Assets/Images/win20badge.png"],
            ]}
          /> */}
        </div>
      </div>
      {/* <div className="activity-section align">
          <div className="status-container">
            <ProfileCard
              username="Hazem Adel"
              bio="Student at FCAI - Cairo University | ECPC’23 Champion - Candidate Master @Codeforces"
              worldRank={5}
              followers={5}
              level={5}
              percentage={30}
              userImg="/Assets/Images/Hazem Adel.jpg"
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
          <div className="blogs-container">
            {[1, 2, 3, 4, 5].map(() => (
              <Blog />
            ))}
          </div>
          <div className="events-container">
             <EventCard title="Ranked Match" btn_title="Battle" details="Challenge your skills and climb the ranks with the option to
                play in a competitive ranked match." img="/Assets/Images/battle.png"/>
                <EventCard title="Casual Match" btn_title="Battle" details="Empower players to create their perfect match by allowing them to customize every aspect." img="/Assets/Images/battle.png"/>
             <FollowCard/>
          </div>
        </div> */}
    </div>
  );
};

export default Home;
