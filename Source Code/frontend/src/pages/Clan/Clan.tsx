import { NavBar } from "../../components";
import "./scss/clan.css";

const Clan = () => {
  return (
    <>
      <NavBar idx={3} />
      <div className="clan-div">
        <div className="c-info">
          <div className="c-box">
            <div className="c-header">
              <h3 className="c-title">Members</h3>
              <img
                className="ch-icn"
                src="/Assets/SVG/view-all.svg"
                alt="ViewAll"
              />
            </div>
            <div className="m-box">
              <div className="member">
                <div className="m-img">
                  <img src="/Assets/Images/defAv.png" alt="profilePhoto" />
                </div>
                <div className="m-info">
                  <span className="m-usn">Username</span>
                  <span className="m-usi">Information about the user</span>
                </div>
              </div>
              <div className="member">
                <div className="m-img">
                  <img src="/Assets/Images/defAv.png" alt="profilePhoto" />
                </div>
                <div className="m-info">
                  <span className="m-usn">Username</span>
                  <span className="m-usi">Information about the user</span>
                </div>
              </div>
              <div className="member">
                <div className="m-img">
                  <img src="/Assets/Images/defAv.png" alt="profilePhoto" />
                </div>
                <div className="m-info">
                  <span className="m-usn">Username</span>
                  <span className="m-usi">Information about the user</span>
                </div>
              </div>
            </div>
          </div>
          <div className="c-box">
            <div className="c-header">
              <h3 className="c-title">Top rated in the clan</h3>
              <img
                className="ch-icn"
                src="/Assets/SVG/view-all.svg"
                alt="ViewAll"
              />
            </div>
            <div className="rm-box">
              <div className="member">
                <span className="rm-usn">1. Username</span>
                <span className="rm-usp">2437</span>
              </div>
              <div className="member">
                <span className="rm-usn">2. Username</span>
                <span className="rm-usp">1762</span>
              </div>
              <div className="member">
                <span className="rm-usn">3. Username</span>
                <span className="rm-usp">1249</span>
              </div>
            </div>
          </div>
        </div>
        <div className="c-chat">
          <div className="ch-header">
            <div className="cl-img">
              <img src="/Assets/SVG/code.svg" alt="Code" />
            </div>
            <div className="cl-i">
              <h3 className="cln">Clan Name</h3>
              <p className="cld">short description</p>
            </div>
          </div>
          <div className="ch">
            <div className="msg-box fr">
              <div className="u-img">
                <img src="/Assets/Images/defAv.png" alt="profilePhoto" />
              </div>
              <div className="msg-info">
                <h4 className="msg-usn">User1</h4>
                <p className="msg">
                  This is a test message, not an online one.
                </p>
              </div>
            </div>
            <div className="msg-box to">
              <div className="u-img">
                <img src="/Assets/Images/defAv.png" alt="profilePhoto" />
              </div>
              <div className="msg-info">
                <h4 className="msg-usn">User2</h4>
                <p className="msg">
                  This is a test response, not an online one.
                </p>
              </div>
            </div>
          </div>
          <form className="ch-msg">
            <textarea placeholder="type a message here"></textarea>
            <button type="submit">
              <img src="/Assets/SVG/send.svg" alt="Send" />
            </button>
          </form>
        </div>
      </div>
    </>
  );
};

export default Clan;
