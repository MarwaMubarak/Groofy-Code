import * as yup from "yup";

const registerSchema = yup.object().shape({
  username: yup
    .string()
    .trim()
    .min(4, "Username should be at least 4 characters")
    .max(100, "Username shouldn't be more than 100 characters")
    .required("Username is required"),
  email: yup
    .string()
    .trim()
    .min(4, "Email should be at least 4 characters")
    .max(255, "Email shouldn't be more than 255 characters")
    .email("Please enter a valid email")
    .required("Email is required"),
  password: yup
    .string()
    .trim()
    .matches(
      new RegExp("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"),
      "Password must contain at least 8 characters, one uppercase, one lowercase and one number"
    )
    .required("Password is required"),
  confirmPassword: yup
    .string()
    .oneOf([yup.ref("password"), undefined], "Passwords must match")
    .required("Confirming Password is required"),
});

const loginSchema = yup.object().shape({
  usernameOrEmail: yup
    .string()
    .trim()
    .test("validateEmail", "Invalid email", (value) => {
      if (value && value.includes("@")) {
        return yup.string().trim().email().isValidSync(value);
      }
      return yup.string().trim().isValidSync(value);
    })
    .required("This field is required"),
  password: yup.string().trim().required("Password is required"),
});

export { registerSchema, loginSchema };
