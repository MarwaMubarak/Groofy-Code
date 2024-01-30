import { Link, useNavigate } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/login/login.css";
import { useDispatch } from "react-redux";
import { authThunks } from "../../store/actions";
import { loginSchema } from "../../shared/schemas";
import { useFormik } from "formik";
import { useRef } from "react";
import { Toast } from "primereact/toast";
import { AxiosError } from "axios";

const Login = () => {
  const toast = useRef<Toast>(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const formHandler = useFormik({
    initialValues: {
      usernameOrEmail: "",
      password: "",
    },
    validationSchema: loginSchema,
    onSubmit: (values, actions) => {
      const ret = dispatch(authThunks.login(values) as any);
      if (ret instanceof Promise) {
        ret.then((res: any) => {
          if (res instanceof AxiosError) {
            actions.resetForm({ values: { ...values, password: "" } });
            (toast.current as any)?.show({
              severity: "error",
              summary: "Failed",
              detail: res.response?.data?.message,
              life: 1500,
            });
          } else {
            console.log("Message", res);
            (toast.current as any)?.show({
              severity: "success",
              summary: "Success",
              detail: "Login successful",
              life: 1500,
            });
            setTimeout(() => {
              actions.resetForm();
              localStorage.setItem("user", JSON.stringify(res.payload));
              navigate("/");
            }, 700);
          }
        });
      }
    },
  });

  return (
    <div className="align">
      <Toast ref={toast} />
      <div className="login-div">
        <div className="auth-title">
          Login as a <span>Groofy</span>
        </div>
        <form className="auth-form" onSubmit={formHandler.handleSubmit}>
          <GroofyField
            giText="Email/Username"
            giPlaceholder="Enter your email or username"
            giType="text"
            giValue={formHandler.values.usernameOrEmail}
            onChange={formHandler.handleChange("usernameOrEmail")}
            onBlur={formHandler.handleBlur("usernameOrEmail")}
            errState={
              (formHandler.errors.usernameOrEmail &&
                formHandler.touched.usernameOrEmail) ||
              false
            }
            errMsg={formHandler.errors.usernameOrEmail}
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
