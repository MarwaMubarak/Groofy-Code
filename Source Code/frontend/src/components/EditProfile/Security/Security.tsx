import React, { useRef } from "react";
import { useFormik } from "formik";
import { passwordSchema } from "../../../shared/schemas";
import { Toast } from "primereact/toast";
import { useDispatch } from "react-redux";
import { userThunks } from "../../../store/actions";
import { AxiosError } from "axios";
import classes from "./scss/security.module.css";
import { Password } from "primereact/password";
import { classNames } from "primereact/utils";
import { Button } from "primereact/button";
import { Divider } from "primereact/divider";

const Security = () => {
  const toast = useRef<Toast>(null);
  const dispatch = useDispatch();

  const formikHandle = useFormik({
    initialValues: {
      oldpassword: "",
      newpassword: "",
      confirmpassword: "",
    },
    validationSchema: passwordSchema,
    onSubmit: (values) => {
      const ret = dispatch(
        userThunks.changePassword(
          values.oldpassword,
          values.newpassword,
          values.confirmpassword
        ) as any
      );
      if (ret instanceof Promise) {
        ret.then((res: any) => {
          console.log(res);
          if (res instanceof AxiosError) {
            toast.current?.show({
              severity: "error",
              summary: "Error Message",
              detail: res.response?.data?.message,
              life: 3000,
            });
          } else {
            toast.current?.show({
              severity: "success",
              summary: "Success Message",
              detail: res.data.message,
              life: 3000,
            });
          }
        });
      }
    },
  });
  const header = <div className={classes.header}>Pick a password</div>;

  const footer = (
    <>
      <Divider />
      <p className={classes.p}>Suggestions</p>
      <ul className={classes.footer}>
        <li>At least one lowercase</li>
        <li>At least one uppercase</li>
        <li>At least one numeric</li>
        <li>Minimum 8 characters</li>
      </ul>
    </>
  );
  return (
    <div className={classes.edit_info}>
      <Toast ref={toast} />
      <h3 className={classes.edit_header}>Security</h3>
      <form
        className={classes.edit_content}
        onSubmit={formikHandle.handleSubmit}
      >
        <div className={classes.passwordField}>
          <h3>Old password</h3>
          <Password
            value={formikHandle.values.oldpassword}
            className={classNames({
              "p-invalid":
                formikHandle.touched.oldpassword &&
                formikHandle.errors.oldpassword,
            })}
            style={{ color: "black" }}
            feedback={false}
            onChange={formikHandle.handleChange("oldpassword")}
            toggleMask
            placeholder="Enter your old password"
          />
          {formikHandle.touched.oldpassword &&
            formikHandle.errors.oldpassword && (
              <span className={classes.err_msg}>
                {formikHandle.errors.oldpassword}
              </span>
            )}
        </div>
        <div className={classes.passwordField}>
          <h3>New password</h3>
          <Password
            value={formikHandle.values.newpassword}
            className={classNames({
              "p-invalid":
                formikHandle.touched.newpassword &&
                formikHandle.errors.newpassword,
            })}
            feedback={true}
            onChange={formikHandle.handleChange("newpassword")}
            toggleMask
            placeholder="Enter your new password"
            header={header}
            footer={footer}
          />
          {formikHandle.touched.newpassword &&
            formikHandle.errors.newpassword && (
              <span className={classes.err_msg}>
                {formikHandle.errors.newpassword}
              </span>
            )}
        </div>
        <div className={classes.passwordField} style={{ marginBottom: "20px" }}>
          <h3>Confirm password</h3>
          <Password
            value={formikHandle.values.confirmpassword}
            className={classNames({
              "p-invalid":
                formikHandle.touched.confirmpassword &&
                formikHandle.errors.confirmpassword,
            })}
            style={{}}
            header="Confirm password"
            feedback={false}
            onChange={formikHandle.handleChange("confirmpassword")}
            toggleMask
            placeholder="Enter your confirmation password"
          />
          {formikHandle.touched.confirmpassword &&
            formikHandle.errors.confirmpassword && (
              <span className={classes.err_msg}>
                {formikHandle.errors.confirmpassword}
              </span>
            )}
        </div>
        <Button className={classNames(classes.groofybtn)} label="Save" />
      </form>
    </div>
  );
};

export default Security;
