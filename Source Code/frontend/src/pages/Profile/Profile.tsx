import { useEffect, useRef } from "react";
import { SideBar, GroofyHeader, GBtn, PostsContainer } from "../../components";
import { useSelector } from "react-redux";
import ReactCountryFlag from "react-country-flag";
import { Toast } from "primereact/toast";
import { Image } from "primereact/image";
import { postActions } from "../../store/slices/post-slice";
import { userThunks } from "../../store/actions";
import { useDispatch } from "react-redux";
import { Link, useParams } from "react-router-dom";
import FormatDate from "../../shared/functions/format-date";
import "./scss/profile.css";

const Profile = () => {
  const dispatch = useDispatch();
  const toast = useRef<Toast>(null);
  const user = useSelector((state: any) => state.auth.user);
  dispatch(postActions.setStatus(""));
  dispatch(postActions.setMessage(""));
  const { username: userProfile } = useParams();

  useEffect(() => {
    const getUser = async () => {
      await dispatch(userThunks.getUser(userProfile!) as any);
    };
    getUser();
  }, [dispatch, userProfile]);

  return (
    <div className="newprofile-container">
      <Toast ref={toast} />
      <SideBar idx={1} />
      <div className="userprofile align">
        <GroofyHeader />
        <div className="up-info">
          <div className="up-info-img">
            <Image src={user.photo.url} alt="Image" width="160" preview />
          </div>
          <div className="up-info-details">
            <div className="up-info-d-box">
              <h3>{user.username}</h3>
              {userProfile === user.username && (
                <Link to="/profile/edit">
                  <div className="up-info-d-box-edit">
                    <img src="/Assets/SVG/edit.svg" alt="EditBtn" />
                    <span>Edit</span>
                  </div>
                </Link>
              )}
            </div>
            <h4>
              Hazem Adel, Giza, Egypt
              <ReactCountryFlag
                countryCode="EG"
                svg
                style={{
                  width: "1em",
                  height: "1em",
                  marginLeft: "8px",
                }}
                title="Egypt"
              />
            </h4>
            <p>
              Dedicated competitive programmer excelling in algorithmic mastery
              and problem-solving. Consistently achieving top ranks in coding
              competitions. Driven by a passion for code optimization and
              continuous improvement.
            </p>
            {userProfile !== user.username && (
              <div className="up-info-details-controls">
                <GBtn
                  btnText="Message"
                  icnSrc="/Assets/SVG/message.svg"
                  clickEvent={() => {}}
                />
                <GBtn
                  btnText="Add Friend"
                  icnSrc="/Assets/SVG/addfriend.svg"
                  clickEvent={() => {}}
                />
              </div>
            )}
          </div>
        </div>
        <div className="userprofile-side">
          <div className="up-side-left">
            <div className="media-section">
              <div
                className={`media-selectors ${userProfile === user.username}`}
              >
                <div className="ms active">
                  <h3>Posts</h3>
                </div>
                {userProfile === user.username && (
                  <div className="ms">
                    <h3>Friends</h3>
                  </div>
                )}

                <div className="ms">
                  <h3>Clan</h3>
                </div>
                <div className="ms">
                  <h3>History</h3>
                </div>
              </div>
              <PostsContainer toast={toast} self={false} />
            </div>
          </div>
          <div className="up-side-right">
            <div className="profile-section">
              <div className="ps-info">
                <div className="ps-header">
                  <h3>Info</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className="info-btn"
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className="ps-container-box">
                  <div className="psi-single-details">
                    <span>
                      <img src="/Assets/SVG/calendar.svg" alt="calender" />
                      Joined
                      <span className="beside">
                        {FormatDate(user.createdAt)}
                      </span>
                    </span>
                  </div>
                  <div className="psi-single-details">
                    <span>
                      World Rank: <span className="beside"> {user.rank}</span>
                    </span>
                  </div>
                  <div className="psi-single-details">
                    <span>
                      Last Seen:
                      <span className="ls">
                        {user.isOnline ? "Online" : "Offline"}
                      </span>
                    </span>
                  </div>
                  <div className="psi-single-details">
                    <span>
                      Friends:
                      <span className="friends">
                        {user.friends?.length || 0}
                      </span>
                    </span>
                  </div>
                </div>
              </div>
              <div className="ps-info">
                <div className="ps-header">
                  <h3>Statistics</h3>
                  <abbr title="Info">
                    <img
                      src="/Assets/SVG/info.svg"
                      className="info-btn"
                      alt="Info"
                    />
                  </abbr>
                </div>
                <div className="ps-container-box">
                  <div className="psi-single-details">
                    <span>
                      <img
                        src="/Assets/Images/battleicon.png"
                        alt="StatsIcon"
                      />
                      Total Matches
                    </span>
                    <span className="any">800</span>
                  </div>
                  <div className="psi-single-details">
                    <span>
                      <img
                        src="/Assets/Images/Yellow_trophy.png"
                        alt="StatsIcon"
                      />
                      Highest Trophies
                    </span>
                    <span className="any">5030</span>
                  </div>
                  <div className="psi-single-details">
                    <span>Wins</span>
                    <span className="any">733</span>
                  </div>
                  <div className="psi-single-details">
                    <span>Loses</span>
                    <span className="any">52</span>
                  </div>
                  <div className="psi-single-details">
                    <span>Draws</span>
                    <span className="any">15</span>
                  </div>
                </div>
              </div>
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
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
