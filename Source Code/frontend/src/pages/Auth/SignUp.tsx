import { Link } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/signup/signup.css";

const SignUp = () => {
  return (
    <div className="align">
      <div className="signup-div">
        <div className="features">
          <div className="ft-title">
            Groofy<span>Code</span>
          </div>
          <div className="ft-container">
            <div className="ft-box">
              <div className="ftb-icn">
                <img src="/Assets/SVG/badgeIcon.svg" alt="BadgeIcon" />
              </div>
              <div className="ftb-info">
                <h4>Achieve, Earn, and Thrive</h4>
                <p>
                  Earn badges and achievements as you tackle coding challenges,
                  participate in matches, and contribute to the community.
                </p>
              </div>
            </div>
            <div className="ft-box">
              <div className="ftb-icn">
                <img src="/Assets/SVG/codeIcon.svg" alt="BadgeIcon" />
              </div>
              <div className="ftb-info">
                <h4>Challenge Your Skills</h4>
                <p>
                  Dive into a world of coding challenges suited for all levels,
                  from beginners to experts. Prove your prowess, learn, and
                  compete with fellow enthusiasts.
                </p>
              </div>
            </div>
            <div className="ft-box">
              <div className="ftb-icn">
                <img src="/Assets/SVG/shieldIcon.svg" alt="ShieldIcon" />
              </div>
              <div className="ftb-info">
                <h4>Unite with Coding Clans</h4>
                <p>
                  Create your own or become part of a community that shares your
                  interests. Collaborate, discuss, solve problems, and compete
                  as a team.
                </p>
              </div>
            </div>
          </div>
        </div>
        <div className="auth">
          <div className="auth-title">
            Sign up as a <span>Groofy</span>
          </div>
          <form className="auth-form">
            <GroofyField
              giText="Username"
              giPlaceholder="Enter your username"
              giType="text"
            />
            <GroofyField
              giText="Email"
              giPlaceholder="Enter your email"
              giType="email"
            />
            <GroofyField
              giText="Password"
              giPlaceholder="Enter your password"
              giType="password"
            />
            <GroofyField
              giText="Confirm Password"
              giPlaceholder="Confirm your password"
              giType="password"
            />
            <div className="f-sbmt">
              <GBtn btnText="Create new account" clickEvent={() => {}} />
              <span className="alrg">
                Already have an account?<Link to="/login">Login</Link>
              </span>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
