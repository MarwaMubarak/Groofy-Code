import { Link, useNavigate } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/login/login.css";
import { useDispatch, useSelector } from "react-redux";
import { authThunks } from "../../store/actions";
import { loginSchema } from "../../shared/schemas";
import { useFormik } from "formik";
import { useEffect, useRef } from "react";
import { Toast } from "primereact/toast";

const Login = () => {
  const toast = useRef<Toast>(null);
  const error = useSelector((state: any) => state.auth.errorMessage);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const formHandler = useFormik({
    initialValues: {
      email: "",
      password: "",
    },
    validationSchema: loginSchema,
    onSubmit: async (values, actions) => {
      try {
        await dispatch(authThunks.login(values) as any);

        // Show success toast
        (toast.current as any)?.show({
          severity: "success",
          summary: "Success",
          detail: "Login successful",
          life: 1500,
        });

        // Delay navigation after toast appears and disappears
        setTimeout(() => {
          actions.resetForm();
          // navigate("/");
        }, 2000); // Adjusted time to account for toast display time
      } catch (error) {
        // Show error toast
        (toast.current as any)?.show({
          severity: "error",
          summary: "Failed",
          detail: error, // Assuming error has a message property
          life: 1500,
        });
        actions.resetForm({ values: { ...values, password: "" } });
        dispatch(authThunks.setErrorMessage("") as any);
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
