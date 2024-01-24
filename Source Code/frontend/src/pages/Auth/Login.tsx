import { Link } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/login/login.css";
import { useState } from "react";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLoginClick = async (event: { preventDefault: () => void }) => {
    event.preventDefault();
    try {
      console.log("Login Clicked");
      console.log(email, password);

      if (email === "" || password === "") {
        alert("Please fill all the fields");
      }
      const response = await fetch("http://localhost:8000/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: email,
          password: password,
        }),
      });
      console.log(response, "response");
      const data = await response.json();
      if (data.error) {
        alert(data.error);
        return;
      }
      console.log("Login Successfull", data);
    } catch (e) {
      console.log(e);
    }
  };
  const handleEmailChange = (e: any) => {
    try {
      console.log("Email Changed");
      setEmail(e);
    } catch (e) {
      console.log(e);
    }
  };
  const handlePasswordChange = (e: any) => {
    try {
      console.log("Password Changed");
      setPassword(e);
    } catch (e) {
      console.log(e);
    }
  };
  return (
    <div className="align">
      <div className="login-div">
        <div className="auth-title">
          Login as a <span>Groofy</span>
        </div>
        <form className="auth-form">
          <GroofyField
            giText="Email"
            giPlaceholder="Enter your username or email"
            giType="email"
            onChange={handleEmailChange}
          />
          <GroofyField
            giText="Password"
            giPlaceholder="Enter your password"
            giType="password"
            onChange={handlePasswordChange}
          />
          <div className="f-sbmt">
            <GBtn btnText="Login" clickEvent={handleLoginClick} />
            <Link to="/forgetpass" className="frg-pass">
              Forget Password?
            </Link>
            <span className="alrg">
              Don't an account?<Link to="/signup">Sign Up</Link>
            </span>
            SignUp
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
