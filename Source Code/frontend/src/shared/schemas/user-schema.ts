import * as yup from "yup";

const userSchema = yup.object().shape({
  displayName: yup
    .string()
    .trim()
    .max(100, "Display name must be between 1 and 100 characters")
    .required("Display name is required"),
  country: yup
    .string()
    .trim()
    .max(100, "Country must be between 1 and 100 characters")
    .required("Country is required"),
  bio: yup.string().trim().max(1000, "Bio can't exceed 1000 characters"),
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
    .oneOf([yup.ref("newpassword"), undefined], "Passwords must match")
    .required("Confirming Password is required"),
});

export { userSchema, passwordSchema };
