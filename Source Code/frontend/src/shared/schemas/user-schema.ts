import * as yup from "yup";

const userSchema = yup.object().shape({
  firstname: yup
    .string()
    .trim()
    .max(100, "First Name shouldn't exceed 100 characters"),
  lastname: yup
    .string()
    .trim()
    .max(100, "Last Name shouldn't exceed 100 characters"),
  city: yup.string().trim().max(40, "City shouldn't exceed 40 characters"),
  bio: yup.string().trim().max(400, "Bio shouldn't exceed 400 characters"),
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
