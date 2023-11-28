import { Link } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/login/login.css";

const SignUp = () => {
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
          />
          <GroofyField
            giText="Password"
            giPlaceholder="Enter your password"
            giType="password"
          />
          <div className="f-sbmt">
            <GBtn btnText="Login" clickEvent={() => {}} />
            <Link to="/forgetpass" className="frg-pass">
              Forget Password?
            </Link>
            <span className="alrg">
              Don't an account?<Link to="/signup">Sign Up</Link>
            </span>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SignUp;
