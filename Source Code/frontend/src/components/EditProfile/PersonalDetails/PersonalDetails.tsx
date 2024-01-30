import { useFormik } from "formik";
import { Toast } from "primereact/toast";
import { ChangeEvent, useRef } from "react";
import { useSelector } from "react-redux";
import { userSchema } from "../../../shared/schemas/user-schema";
import GroofyField from "../../Auth/GroofyField/GroofyField";
import { Dropdown, DropdownChangeEvent } from "primereact/dropdown";
import GBtn from "../../GBtn/GBtn";
import "./scss/global/personaldetails.css";
import classes from "./scss/private/personaldetails.module.css";
import { userThunks } from "../../../store/actions";
import { AxiosError } from "axios";
import { EditInfo } from "../../../store/actions/user-actions";
import { useDispatch } from "react-redux";

interface Country {
  name: string;
  code: string;
}

const PersonalDetails = () => {
  const toast = useRef<Toast>(null);
  const user = useSelector((state: any) => state.auth.user);
  const dispatch = useDispatch();
  const countries: Country[] = [
    { name: "Australia", code: "AU" },
    { name: "Brazil", code: "BR" },
    { name: "China", code: "CN" },
    { name: "Egypt", code: "EG" },
    { name: "France", code: "FR" },
    { name: "Germany", code: "DE" },
    { name: "India", code: "IN" },
    { name: "Japan", code: "JP" },
    { name: "Spain", code: "ES" },
    { name: "United States", code: "US" },
  ];

  const formikHandle = useFormik({
    initialValues: {
      firstName: "",
      lastName: "",
      city: "",
      bio: "",
      country: { name: "", code: "" },
    },
    validationSchema: userSchema,
    onSubmit: (values) => {
      console.log("VALUES", values);

      const ret = dispatch(userThunks.updateUser(user._id, values) as any);
      if (ret instanceof Promise) {
        ret.then((res: any) => {
          if (res instanceof AxiosError) {
            (toast.current as any)?.show({
              severity: "error",
              summary: "Failed",
              detail: res.response?.data?.message,
              life: 1500,
            });
          } else {
            (toast.current as any)?.show({
              severity: "success",
              summary: "Success",
              detail: res.payload.message,
              life: 1500,
            });
            // setTimeout(() => {
            // actions.resetForm();
            // localStorage.setItem("user", JSON.stringify(res.payload));
            // }, 700);
          }
        });
      }
    },
  });
  const selectedCountryTemplate = (option: Country, props: any) => {
    if (option) {
      return (
        <div className="flex align-items-center">
          <div>{option.name}</div>
        </div>
      );
    }
    return <span>{props.placeholder}</span>;
  };
  const countryOptionTemplate = (option: Country) => {
    return (
      <div className="flex align-items-center">
        <div>{option.name}</div>
      </div>
    );
  };

  const handleCombinedChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    formikHandle.handleChange(e);
    autoExpand(e.target);
  };
  const autoExpand = (textarea: HTMLTextAreaElement) => {
    textarea.style.height = "auto";
    textarea.style.height = textarea.scrollHeight + "px";
  };
  return (
    <div className={classes.edit_info}>
      <h3 className={classes.edit_header}>Personal Details</h3>
      <form
        className={classes.edit_content}
        onSubmit={formikHandle.handleSubmit}
      >
        <div className={classes.editprofile_photo}>
          <img src={user.photo.url} alt="profile_photo" />
          <input id="upload" type="file" hidden />
          <label htmlFor="upload">
            <i
              className="pi pi-upload
"
            />
            Upload photo
          </label>
        </div>
        <GroofyField
          giText="First name"
          giValue={formikHandle.values.firstName}
          giPlaceholder="Enter your first name"
          giType="text"
          errState={
            formikHandle.touched.firstName && formikHandle.errors.firstName
              ? true
              : false
          }
          onChange={formikHandle.handleChange("firstName")}
          onBlur={formikHandle.handleBlur("firstName")}
          errMsg={formikHandle.errors.firstName}
        />
        <GroofyField
          giText="Last name"
          giValue={formikHandle.values.lastName}
          giPlaceholder="Enter your last name"
          giType="text"
          errState={
            formikHandle.touched.lastName && formikHandle.errors.lastName
              ? true
              : false
          }
          onChange={formikHandle.handleChange("lastName")}
          onBlur={formikHandle.handleBlur("lastName")}
          errMsg={formikHandle.errors.lastName}
        />
        <GroofyField
          giText="City"
          giValue={formikHandle.values.city}
          giPlaceholder="Enter your city"
          giType="text"
          errState={
            formikHandle.touched.city && formikHandle.errors.city ? true : false
          }
          onChange={formikHandle.handleChange("city")}
          onBlur={formikHandle.handleBlur("city")}
          errMsg={formikHandle.errors.city}
        />
        <div className={classes.bio_content}>
          <h1>Bio</h1>
          <textarea
            id="bio"
            value={formikHandle.values.bio}
            placeholder="Share your coding insights and experiences"
            onChange={handleCombinedChange}
            onBlur={formikHandle.handleBlur("bio")}
            maxLength={500}
          />
        </div>
        <div className={classes.country_selector}>
          <div className={classes.cs_title}>Select your country:</div>
          <Dropdown
            id="country"
            value={formikHandle.values.country}
            onChange={(e: DropdownChangeEvent) =>
              formikHandle.setFieldValue("country", e.value)
            }
            options={countries}
            optionLabel="name"
            placeholder="Select a Country"
            filter
            valueTemplate={selectedCountryTemplate}
            itemTemplate={countryOptionTemplate}
            className="w-full md:w-14rem"
          />
        </div>
        <GBtn
          btnText="Save"
          btnType={true}
          clickEvent={() => {}}
          btnState={formikHandle.isSubmitting}
        />
      </form>
    </div>
  );
};

export default PersonalDetails;
