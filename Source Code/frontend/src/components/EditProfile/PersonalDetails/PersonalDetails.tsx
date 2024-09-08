import { useFormik } from "formik";
import { Toast } from "primereact/toast";
import { ChangeEvent, useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import { userSchema } from "../../../shared/schemas/user-schema";
import GroofyField from "../../Auth/GroofyField/GroofyField";
import { Dropdown, DropdownChangeEvent } from "primereact/dropdown";
import { userThunks } from "../../../store/actions";
import { useDispatch } from "react-redux";
import { EditInfo } from "../../../store/actions/user-actions";
import { AxiosError } from "axios";
import classes from "./scss/personaldetails.module.css";
import { Button } from "primereact/button";
import { classNames } from "primereact/utils";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";
import styles from "./scss/dropdown.module.css";
import { ProfileImage } from "../..";

interface Country {
  name: string;
  code: string;
}

const PersonalDetails = () => {
  const toast = useRef<Toast>(null);
  const [expanded, setExpanded] = useState(false);
  const user = useSelector((state: any) => state.auth.user);
  const isUploading = useSelector((state: any) => state.auth.isUploading);
  const isDeleting = useSelector((state: any) => state.auth.isDeleting);
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
      displayName: user.displayName,
      bio: user.bio?.toString() || "",
      country: countries.find((country) => country.name === user.country),
    },
    validationSchema: userSchema,
    onSubmit: (values) => {
      const editInfo: EditInfo = {
        displayName: values.displayName,
        bio: values.bio,
        country: values.country!.name,
      };
      const ret = dispatch(userThunks.updateUser(editInfo) as any);
      if (ret instanceof Promise) {
        ret.then((res: any) => {
          if (res instanceof AxiosError) {
            (toast.current as any)?.show({
              severity: "error",
              summary: "Failed",
              detail: res.response?.data?.message,
              life: 3000,
            });
          } else {
            (toast.current as any)?.show({
              severity: "success",
              summary: "Success",
              detail: res.data.message,
              life: 3000,
            });
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

  const handleDeletePhoto = async () => {
    return await dispatch(userThunks.changePhoto(null) as any);
  };

  const handleFileUpload = async (e: ChangeEvent<HTMLInputElement>) => {
    const userPhoto = e.target.files![0];
    return await dispatch(userThunks.changePhoto(userPhoto) as any);
  };

  const confirmDeletePhoto = () => {
    confirmDialog({
      message: "Are you sure you want to delete your profile photo ?",
      header: "Confirmation",
      icon: "pi pi-exclamation-triangle",
      accept: () =>
        handleDeletePhoto()
          .then((res: any) => {
            if (res.status === "success") {
              toast.current?.show({
                severity: "success",
                summary: res.status,
                detail: res.message,
                life: 3000,
              });
            }
          })
          .catch((error: any) => {
            toast.current?.show({
              severity: "error",
              summary: error.status,
              detail: error.message,
              life: 3000,
            });
          }),
      reject: () => null,
    });
  };

  return (
    <div className={classes.edit_info}>
      <Toast ref={toast} />
      <ConfirmDialog />
      <h3 className={classes.edit_header}>Personal Details</h3>
      <form
        className={classes.edit_content}
        onSubmit={formikHandle.handleSubmit}
      >
        <div className={classes.editprofile_photo}>
          {user.photoUrl !== null ? (
            <img src={user.photoUrl} alt="profile_photo" />
          ) : (
            <ProfileImage
              photoUrl={user.photoUrl}
              username={user.username}
              style={{
                backgroundColor: user.accountColor,
                width: "170px",
                height: "170px",
                fontSize: "60px",
                marginBottom: "25px",
              }}
              canClick={false}
            />
          )}

          <div
            className={`${classes.change_photo_btn} ${expanded && classes.y}`}
            onClick={() => setExpanded(!expanded)}
          >
            Change photo
          </div>
          <div className={`${classes.photo_menu} ${expanded && classes.y}`}>
            <input
              type="file"
              id="up-btn"
              style={{ display: "none" }}
              disabled={isUploading || isDeleting}
              onChange={(e: any) => {
                handleFileUpload(e)
                  .then((res: any) => {
                    (toast.current as any)?.show({
                      severity: "success",
                      summary: res.status,
                      detail: res.message,
                      life: 3000,
                    });
                  })
                  .catch((err: any) => {
                    (toast.current as any)?.show({
                      severity: "error",
                      summary: err.status,
                      detail: err.message,
                      life: 3000,
                    });
                  });
              }}
            />
            <label
              className={`${classes.photo_menu_item} ${
                isUploading && classes.dis
              }`}
              htmlFor="up-btn"
            >
              <i
                className={`${
                  isUploading ? "pi pi-spin pi-spinner" : "bi bi-upload"
                }`}
              ></i>
              <span>{isUploading ? "Uploading..." : "Upload"}</span>
            </label>
            <div
              className={classes.photo_menu_item}
              onClick={confirmDeletePhoto}
            >
              <i
                className={`${
                  isDeleting ? "pi pi-spin pi-spinner" : "bi bi-trash3"
                }`}
              ></i>
              <span>{isDeleting ? "Removing..." : "Remove"}</span>
            </div>
          </div>
        </div>
        <GroofyField
          giText="Display name"
          giValue={formikHandle.values.displayName}
          giPlaceholder="Enter your display name"
          giType="text"
          errState={
            formikHandle.touched.displayName && formikHandle.errors.displayName
              ? true
              : false
          }
          onChange={formikHandle.handleChange("displayName")}
          onBlur={formikHandle.handleBlur("displayName")}
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
            className={classes.drop_down_div}
            panelClassName={styles.drop_down_panel}
          />
        </div>
        <Button
          className={classNames(classes.groofybtn)}
          type="submit"
          label="Save"
        />
      </form>
    </div>
  );
};

export default PersonalDetails;
