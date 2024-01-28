import {
  GBtn,
  GroofyHeader,
  SideBar,
  Gamemode,
  SinglePost,
} from "../../components";
import { ChangeEvent, useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import postThunks from "../../store/actions/post-actions";
import "./scss/home.css";
import { Toast } from "primereact/toast";
import {
  Paginator,
  PaginatorCurrentPageReportOptions,
  PaginatorPageChangeEvent,
  PaginatorRowsPerPageDropdownOptions,
} from "primereact/paginator";
import React from "react";
import { Dropdown } from "primereact/dropdown";

const Home = () => {
  const dispatch = useDispatch();
  const [newPostContent, setNewPostContent] = useState("");
  const resStatus = useSelector((state: any) => state.post.status);
  const resMessage = useSelector((state: any) => state.post.message);
  const user = useSelector((state: any) => state.auth.user);
  const allPosts: any[] = useSelector((state: any) => state.post.body);
  const toast = useRef<Toast>(null);
  const [first, setFirst] = useState<number>(0);
  const [rows, setRows] = useState<number>(5);

  const onPageChange = (event: PaginatorPageChangeEvent) => {
    setFirst(event.first);
    setRows(event.rows);
  };

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
      await dispatch(postThunks.getPosts(user["_id"]) as any);
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
  }, [allPosts.length, resMessage, resStatus]);

  const PaginatorTemplate = {
    layout:
      "FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown ",
    RowsPerPageDropdown: (options: PaginatorRowsPerPageDropdownOptions) => {
      const dropdownOptions = [
        { label: 5, value: 5 },
        { label: 10, value: 10 },
        { label: 20, value: 20 },
        { label: 50, value: 50 },
      ];

      return (
        <React.Fragment>
          <span
            className="mx-1"
            style={{ color: "var(--text-color)", userSelect: "none" }}
          >
            Posts per page:{" "}
          </span>
          <Dropdown
            value={options.value}
            options={dropdownOptions}
            onChange={options.onChange}
          />
        </React.Fragment>
      );
    },
    CurrentPageReport: (options: PaginatorCurrentPageReportOptions) => {
      return (
        <span
          style={{
            color: "var(--text-color)",
            userSelect: "none",
            width: "120px",
            textAlign: "center",
          }}
        >
          {options.first} - {options.last} of {options.totalRecords}
        </span>
      );
    },
  };

  return (
    <div className="home-container">
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
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
                {allPosts && allPosts.length > 0 ? (
                  <>
                    {allPosts
                      .slice(
                        Math.min(first, allPosts.length),
                        Math.min(first + rows, allPosts.length)
                      )
                      .map((post) => (
                        <SinglePost
                          key={post["id"]}
                          postUser={user["username"]}
                          postUserImg="/Assets/Images/Hazem Adel.jpg"
                          postContent={post["content"]}
                          postTime={post["createdAt"]}
                          postID={post["id"]}
                          isEdited={post["createdAt"] !== post["updatedAt"]}
                        />
                      ))}
                    <Paginator
                      first={first}
                      rows={rows}
                      totalRecords={allPosts.length}
                      rowsPerPageOptions={[5, 10, 20]}
                      onPageChange={onPageChange}
                      template={PaginatorTemplate}
                      // currentPageReportTemplate="{first} to {last} of {totalRecords} posts"
                    />
                  </>
                ) : (
                  <div className="empty-posts">No posts available.</div>
                )}
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
