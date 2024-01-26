import { Link } from "react-router-dom";
import { GBtn, GroofyField } from "../../components";
import "./scss/signup/signup.css";
import { useDispatch } from "react-redux";
import { authThunks } from "../../store/actions";
import { registerSchema } from "../../shared/schemas";
import { useFormik } from "formik";
// import { useInput } from "../../shared/hooks";

const SignUp = () => {
  const dispatch = useDispatch();
  // const emailRE: RegExp = new RegExp("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
  // const passRE: RegExp = new RegExp(
  //   "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
  // );
  // const usernameValidator = useInput(
  //   (value) =>
  //     value.trim() !== "" &&
  //     value.trim().length >= 4 &&
  //     value.trim().length <= 100
  // );
  // const emailValidator = useInput(
  //   (value) =>
  //     value.trim() !== "" &&
  //     new RegExp(emailRE).test(value) &&
  //     value.trim().length >= 4 &&
  //     value.trim().length <= 255
  // );
  const formHandler = useFormik({
    initialValues: {
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
    },
    validationSchema: registerSchema,
    onSubmit: (values, actions) => {
      // @ts-ignore
      dispatch(authThunks.signup(values));
      actions.resetForm();
    },
  });

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
          <form className="auth-form" onSubmit={formHandler.handleSubmit}>
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
            <div className="f-sbmt">
              <GBtn
                btnText="Create new account"
                clickEvent={() => {}}
                btnType={true}
                btnState={formHandler.isSubmitting}
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
