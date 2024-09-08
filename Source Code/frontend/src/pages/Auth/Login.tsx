import { Link, useNavigate } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import { useDispatch } from "react-redux";
import { authThunks } from "../../store/actions";
import { loginSchema } from "../../shared/schemas";
import { useFormik } from "formik";
import { useRef } from "react";
import { Toast } from "primereact/toast";
import classes from "./scss/login/login.module.css";

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
      const logUser = async () => {
        await dispatch(authThunks.login(values) as any);
      };
      logUser()
        .then(() => {
          (toast.current as any)?.show({
            severity: "success",
            summary: "Success",
            detail: "Login successful",
            life: 1500,
          });
          setTimeout(() => {
            actions.resetForm();
            navigate("/");
          }, 700);
        })
        .catch((error: any) => {
          actions.resetForm({ values: { ...values, password: "" } });
          (toast.current as any)?.show({
            severity: "error",
            summary: "Failed",
            detail: error.response.data.message,
            life: 1500,
          });
        });
    },
  });

  return (
    <div className={classes.align}>
      <Toast ref={toast} />
      <div className={classes.login_div}>
        <div className={classes.auth_title}>
          Login as a <span>Groofy</span>
        </div>
        <form className={classes.auth_form} onSubmit={formHandler.handleSubmit}>
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
          <div className={classes.f_sbmt}>
            <GBtn
              btnText="Login"
              clickEvent={() => {}}
              btnType={true}
              btnState={formHandler.isSubmitting}
            />
            <Link to="/forgetpass" className={classes.frg_pass}>
              Forget Password?
            </Link>
            <span className={classes.alrg}>
              Don't an account?<Link to="/signup">Sign Up</Link>
            </span>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
