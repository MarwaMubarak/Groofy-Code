import { Link } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/signup/signup.css";
import { useState } from "react";

const SignUp = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassowrd] = useState("");

  const onUsernameChange = (e: any) => {
    try {
      setUsername(e);
    } catch (e) {
      console.log(e);
    }
  };
  const onEmailChange = (e: any) => {
    try {
      setEmail(e);
    } catch (e) {
      console.log(e);
    }
  };
  const onPasswordChange = (e: any) => {
    try {
      setPassword(e);
    } catch (e) {
      console.log(e);
    }
  };
  const onConfirmPasswordChange = (e: any) => {
    try {
      setConfirmPassowrd(e);
    } catch (e) {
      console.log(e);
    }
  };

  const handleSignUpClick = async (event: { preventDefault: () => void }) => {
    event.preventDefault();
    try {
      console.log("SignUp Clicked");
      console.log(username, email, password, confirmPassword);
      if (
        username == "" ||
        email == "" ||
        password == "" ||
        confirmPassword == ""
      ) {
        alert("Please fill all the fields");
        return;
      }

      if (password !== confirmPassword) {
        alert("Passwords do not match");
        return;
      }
      const response = await fetch("http://localhost:8000/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: username,
          email: email,
          password: password,
          firstname: "mero",
          lastname: "mero",
          country: "Eg",
        }),
      });

      const data = await response.json();
      if (data.error) {
        alert(data.error);
        return;
      }
      console.log("SignUp Successfull", data);
    } catch (e) {
      console.log(e);
    }
  };

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
              onChange={onUsernameChange}
            />
            <GroofyField
              giText="Email"
              giPlaceholder="Enter your email"
              giType="email"
              onChange={onEmailChange}
            />
            <GroofyField
              giText="Password"
              giPlaceholder="Enter your password"
              giType="password"
              onChange={onPasswordChange}
            />
            <GroofyField
              giText="Confirm Password"
              giPlaceholder="Confirm your password"
              giType="password"
              onChange={onConfirmPasswordChange}
            />
            <div className="f-sbmt">
              <GBtn
                btnText="Create new account"
                clickEvent={handleSignUpClick}
              />
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
