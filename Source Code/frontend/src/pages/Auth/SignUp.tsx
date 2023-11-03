import "./scss/auth.css";

const SignUp = () => {
  return (
    <div className="auth-main">
      <div className="cut"></div>
      <div className="ainfo">
        <span className="atitle">Create a new account</span>
        <form>
          <input type="text" placeholder="Username" />
          <input type="email" placeholder="Email" />
          <input type="password" placeholder="Password" />
          <input type="password" placeholder="Re-enter your password" />
          <button type="submit">Sign Up</button>
        </form>
      </div>
      <div className="auth-img">
        <img src="/Assets/Images/authPic.png" alt="AuthPic" />
      </div>
    </div>
  );
};

export default SignUp;
