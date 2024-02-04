import { GroofyWrapper } from "../../components";
import classes from "./scss/clan.module.css";

const Clan = () => {
  return (
    <GroofyWrapper idx={3}>
      <div className={classes.clan_div}>
        <div className={classes.c_info}>
          <div className={classes.c_box}>
            <div className={classes.c_header}>
              <h3 className={classes.c_title}>Members</h3>
              <img
                className={classes.ch_icn}
                src="/Assets/SVG/view-all.svg"
                alt="ViewAll"
              />
            </div>
            <div className={classes.m_box}>
              <div className={classes.member}>
                <div className={classes.m_info}>
                  <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
                  <span className={classes.m_usn}>Username</span>
                </div>
                <div className={classes.m_online}>Online</div>
              </div>
              <div className={classes.member}>
                <div className={classes.m_info}>
                  <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
                  <span className={classes.m_usn}>Username</span>
                </div>
                <div className={classes.m_online}>Online</div>
              </div>
              <div className={classes.member}>
                <div className={classes.m_info}>
                  <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
                  <span className={classes.m_usn}>Username</span>
                </div>
              </div>
            </div>
          </div>
          <div className={classes.c_box}>
            <div className={classes.c_header}>
              <h3 className={classes.c_title}>Top rated</h3>
              <img
                className={classes.ch_icn}
                src="/Assets/SVG/view-all.svg"
                alt="ViewAll"
              />
            </div>
            <div className={classes.rm_box}>
              <div className={classes.member}>
                <span className={classes.rm_usn}>1. Username</span>
                <span className={classes.rm_usp}>2437</span>
              </div>
              <div className={classes.member}>
                <span className={classes.rm_usn}>2. Username</span>
                <span className={classes.rm_usp}>1762</span>
              </div>
              <div className={classes.member}>
                <span className={classes.rm_usn}>3. Username</span>
                <span className={classes.rm_usp}>1249</span>
              </div>
            </div>
          </div>
        </div>
        <div className={classes.c_chat}>
          <div className={classes.ch_header}>
            <div className={classes.cl_img}>
              <img src="/Assets/SVG/code.svg" alt="Code" />
            </div>
            <div className={classes.cl_i}>
              <h3 className={classes.cln}>Clan Name</h3>
              <p className={classes.cld}>short description</p>
            </div>
          </div>
          <div className={classes.ch}>
            <div className={classes.msg_box + " " + classes.fr}>
              <div className={classes.u_img}>
                <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
              </div>
              <div className={classes.msg_info}>
                <h4 className={classes.msg_usn}>User1</h4>
                <p className={classes.msg}>
                  This is a test message, not an online one.
                </p>
              </div>
            </div>
            <div className={classes.msg_box + " " + classes.to}>
              <div className={classes.u_img}>
                <img src="/Assets/Images/Hazem Adel.jpg" alt="profilePhoto" />
              </div>
              <div className={classes.msg_info}>
                <h4 className={classes.msg_usn}>User2</h4>
                <p className={classes.msg}>
                  This is a test response, not an online one.
                </p>
              </div>
            </div>
          </div>
          <form className={classes.ch_msg}>
            <textarea placeholder="type a message here"></textarea>
            <div className={classes.submit_btn}>
              <button type="submit">
                <img src="/Assets/SVG/send.svg" alt="Send" />
              </button>
            </div>
          </form>
        </div>
      </div>
    </GroofyWrapper>
  );
};

export default Clan;
