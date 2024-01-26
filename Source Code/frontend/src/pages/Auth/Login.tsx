import { Link, useNavigate } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/login/login.css";
import { useDispatch, useSelector } from "react-redux";
import { authThunks } from "../../store/actions";
import { loginSchema } from "../../shared/schemas";
import { useFormik } from "formik";

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const isAuth = useSelector((state: any) => state.auth.isAuthenticated);
  const formHandler = useFormik({
    initialValues: {
      email: "",
      password: "",
    },
    validationSchema: loginSchema,
    onSubmit: (values, actions) => {
      // @ts-ignore
      dispatch(authThunks.login(values));
      if (isAuth) {
        actions.resetForm();
        navigate("/");
      } else {
        actions.resetForm({ values: { ...values, password: "" } });
      }
    },
  });
  return (
    <div className="align">
      <div className="login-div">
        <div className="auth-title">
          Login as a <span>Groofy</span>
        </div>
        <form className="auth-form" onSubmit={formHandler.handleSubmit}>
          <GroofyField
            giText="Email"
            giPlaceholder="Enter your username or email"
            giType="email"
            giValue={formHandler.values.email}
            onChange={formHandler.handleChange("email")}
            onBlur={formHandler.handleBlur("email")}
            errState={
              (formHandler.errors.email && formHandler.touched.email) || false
            }
            errMsg={formHandler.errors.email}
          />
          <GroofyField
            giText="Password"
            giPlaceholder="Enter your password"
            giType="password"
            giValue={formHandler.values.password}
            onChange={formHandler.handleChange("password")}
            onBlur={formHandler.handleBlur("password")}
            errState={
              (formHandler.errors.password && formHandler.touched.password) ||
              false
            }
            errMsg={formHandler.errors.password}
          />
          <div className="f-sbmt">
            <GBtn
              btnText="Login"
              clickEvent={() => {}}
              btnType={true}
              btnState={formHandler.isSubmitting}
            />
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

export default Login;
