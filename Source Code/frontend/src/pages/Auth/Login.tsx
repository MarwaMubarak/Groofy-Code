import "./scss/auth.css";

const Login = () => {
  return (
    <div className="auth-main">
      <div className="cut"></div>
      <div className="ainfo">
        <span className="atitle">Login</span>
        <form>
          <input type="email" placeholder="Email" />
          <input type="password" placeholder="Password" />
          <button type="submit">Login</button>
        </form>
      </div>
      <div className="auth-img">
        <img src="/Assets/authPic.png" alt="AuthPic" />
      </div>
    </div>
  );
};

export default Login;
