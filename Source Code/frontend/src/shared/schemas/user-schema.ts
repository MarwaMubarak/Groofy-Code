import * as yup from "yup";

const userSchema = yup.object().shape({
  firstName: yup
    .string()
    .trim()
    .min(4, "First Name should be at least 4 characters")
    .max(100, "First Name shouldn't be more than 100 characters"),
  lastName: yup
    .string()
    .trim()
    .max(100, "Last Name shouldn't be more than 100 characters"),
  city: yup
    .string()
    .trim()
    .max(40, "City shouldn't be more than 40 characters"),
  bio: yup
    .string()
    .trim()
    .max(400, "Bio shouldn't be more than 400 characters"),
});

const passwordSchema = yup.object().shape({
  oldpassword: yup.string().trim().required("Old password is required"),
  newpassword: yup
    .string()
    .trim()
    .matches(
      new RegExp("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"),
      "Password must contain at least 8 characters, one uppercase, one lowercase and one number"
    )
    .required("New password is required"),
  confirmpassword: yup
    .string()
    .oneOf([yup.ref("password"), undefined], "Passwords must match")
    .required("Confirming Password is required"),
});

export { userSchema, passwordSchema };
