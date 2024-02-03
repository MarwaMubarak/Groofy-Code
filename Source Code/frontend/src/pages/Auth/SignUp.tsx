import { Link, useNavigate } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import { useDispatch } from "react-redux";
import { authThunks } from "../../store/actions";
import { registerSchema } from "../../shared/schemas";
import { useFormik } from "formik";
import { Toast } from "primereact/toast";
import { useRef } from "react";
import classes from "./scss/signup/signup.module.css";

const SignUp = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const toast = useRef<Toast>(null);
  const formHandler = useFormik({
    initialValues: {
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
    },
    validationSchema: registerSchema,
    onSubmit: (values, actions) => {
      const signUser = async () => {
        await dispatch(authThunks.signup(values) as any);
      };
      signUser()
        .then(() => {
          (toast.current as any)?.show({
            severity: "success",
            summary: "Success",
            detail: "Account created successfully",
            life: 1500,
          });
          setTimeout(() => {
            actions.resetForm();
            navigate("/login");
          }, 700);
        })
        .catch((error: any) => {
          actions.resetForm({ values: { ...values } });
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
      <div className={classes.signup_div}>
        <div className={classes.features}>
          <div className={classes.ft_title}>
            Groofy<span>Code</span>
          </div>
          <div className={classes.ft_container}>
            <div className={classes.ft_box}>
              <div className={classes.ftb_icn}>
                <img src="/Assets/SVG/badgeIcon.svg" alt="BadgeIcon" />
              </div>
              <div className={classes.ftb_info}>
                <h4>Achieve, Earn, and Thrive</h4>
                <p>
                  Earn badges and achievements as you tackle coding challenges,
                  participate in matches, and contribute to the community.
                </p>
              </div>
            </div>
            <div className={classes.ft_box}>
              <div className={classes.ftb_icn}>
                <img src="/Assets/SVG/codeIcon.svg" alt="BadgeIcon" />
              </div>
              <div className={classes.ftb_info}>
                <h4>Challenge Your Skills</h4>
                <p>
                  Dive into a world of coding challenges suited for all levels,
                  from beginners to experts. Prove your prowess, learn, and
                  compete with fellow enthusiasts.
                </p>
              </div>
            </div>
            <div className={classes.ft_box}>
              <div className={classes.ftb_icn}>
                <img src="/Assets/SVG/shieldIcon.svg" alt="ShieldIcon" />
              </div>
              <div className={classes.ftb_info}>
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
        <div className={classes.auth}>
          <div className={classes.auth_title}>
            Sign up as a <span>Groofy</span>
          </div>
          <form
            className={classes.auth_form}
            onSubmit={formHandler.handleSubmit}
          >
            <GroofyField
              giText="Username"
              giPlaceholder="Enter your username"
              giType="text"
              giValue={formHandler.values.username}
              onChange={formHandler.handleChange("username")}
              onBlur={formHandler.handleBlur("username")}
              errState={
                (formHandler.errors.username && formHandler.touched.username) ||
                false
              }
              errMsg={formHandler.errors.username}
            />
            <GroofyField
              giText="Email"
              giPlaceholder="Enter your email"
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
            <GroofyField
              giText="Confirm Password"
              giPlaceholder="Confirm your password"
              giType="password"
              giValue={formHandler.values.confirmPassword}
              onChange={formHandler.handleChange("confirmPassword")}
              onBlur={formHandler.handleBlur("confirmPassword")}
              errState={
                (formHandler.errors.confirmPassword &&
                  formHandler.touched.confirmPassword) ||
                false
              }
              errMsg={formHandler.errors.confirmPassword}
            />
            <div className={classes.f_sbmt}>
              <GBtn
                btnText="Create new account"
                clickEvent={() => {}}
                btnType={true}
                btnState={formHandler.isSubmitting}
              />
              <span className={classes.alrg}>
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
