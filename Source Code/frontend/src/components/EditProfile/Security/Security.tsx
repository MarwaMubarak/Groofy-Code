import React, { useRef } from "react";
import GBtn from "../../GBtn/GBtn";
import GroofyField from "../../Auth/GroofyField/GroofyField";
import { useFormik } from "formik";
import { passwordSchema } from "../../../shared/schemas";
import { Toast } from "primereact/toast";
import "./scss/security.css";

const Security = () => {
  const toast = useRef<Toast>(null);

  const formikHandle = useFormik({
    initialValues: {
      oldpassword: "",
      newpassword: "",
      confirmpassword: "",
    },
    validationSchema: passwordSchema,
    onSubmit: (values) => {
      toast.current?.show({
        severity: "success",
        summary: "Success Message",
        detail: "Changes are saved",
      });
    },
  });
  return (
    <div className="edit-info">
      <h3 className="edit-header">Security</h3>
      <form className="edit-content" onSubmit={formikHandle.handleSubmit}>
        <GroofyField
          giText="Old password"
          giValue={formikHandle.values.oldpassword}
          giPlaceholder="Enter your old password"
          giType="text"
          errState={
            formikHandle.touched.oldpassword && formikHandle.errors.oldpassword
              ? true
              : false
          }
          onChange={formikHandle.handleChange("oldpassword")}
          onBlur={formikHandle.handleBlur("oldpassword")}
          errMsg={formikHandle.errors.oldpassword}
        />
        <GroofyField
          giText="New password"
          giValue={formikHandle.values.newpassword}
          giPlaceholder="Enter your new password"
          giType="text"
          errState={
            formikHandle.touched.newpassword && formikHandle.errors.newpassword
              ? true
              : false
          }
          onChange={formikHandle.handleChange("newpassword")}
          onBlur={formikHandle.handleBlur("newpassword")}
          errMsg={formikHandle.errors.newpassword}
        />
        <GroofyField
          giText="Confirm password"
          giValue={formikHandle.values.confirmpassword}
          giPlaceholder="Enter your confirmpassword"
          giType="text"
          errState={
            formikHandle.touched.confirmpassword &&
            formikHandle.errors.confirmpassword
              ? true
              : false
          }
          onChange={formikHandle.handleChange("confirmpassword")}
          onBlur={formikHandle.handleBlur("confirmpassword")}
          errMsg={formikHandle.errors.confirmpassword}
        />
        <GBtn btnText="Save" btnType={true} clickEvent={() => {}} />
      </form>
    </div>
  );
};

export default Security;
