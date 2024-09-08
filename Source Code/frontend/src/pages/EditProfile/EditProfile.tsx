import { useRef, useState } from "react";
import {
  Appearance,
  Notifications,
  PersonalDetails,
  Privacy,
  Security,
  SideBar,
} from "../../components";
import { Toast } from "primereact/toast";
import classes from "./scss/editprofile.module.css";

const EditProfile = () => {
  const toast = useRef<Toast>(null);
  const [activeSection, setActiveSection] = useState(0);
  return (
    <div className={classes.editprofile_container}>
      <Toast ref={toast} />
      <SideBar idx={1} />
      <div className={classes.editprofile_page}>
        <div className={classes.editprofile_settings}>
          <i className="pi pi-cog" />
          <h1>Settings</h1>
        </div>
        <div className={classes.edit_profile}>
          <div className={classes.edit_menu}>
            <div
              className={`${
                classes.edit_section + " " + (!activeSection && classes.active)
              }`}
              onClick={() => setActiveSection(0)}
            >
              <div className={classes.es_img}>
                <i className="pi pi-id-card" />
              </div>
              <span className={classes.es_title}>Personal Details</span>
            </div>
            <div
              className={`${
                classes.edit_section +
                " " +
                (activeSection === 1 && classes.active)
              }`}
              onClick={() => setActiveSection(1)}
            >
              <div className={classes.es_img}>
                <i className="pi pi-palette" />
              </div>
              <span className={classes.es_title}>Appearance & Style</span>
            </div>
            {/* <div
              className={`${
                classes.edit_section +
                " " +
                (activeSection === 2 && classes.active)
              }`}
              onClick={() => setActiveSection(2)}
            >
              <div className={classes.es_img}>
                <i className="pi pi-lock" />
              </div>
              <span className={classes.es_title}>Privacy Preferences</span>
            </div> */}
            <div
              className={`${
                classes.edit_section +
                " " +
                (activeSection === 3 && classes.active)
              }`}
              onClick={() => setActiveSection(3)}
            >
              <div className={classes.es_img}>
                <i className="pi pi-shield" />
              </div>
              <span className={classes.es_title}>Security Settings</span>
            </div>
            {/* <div
              className={`${
                classes.edit_section +
                " " +
                (activeSection === 4 && classes.active)
              }`}
              onClick={() => setActiveSection(4)}
            >
              <div className={classes.es_img}>
                <i className="pi pi-bell" />
              </div>
              <span className={classes.es_title}>Notification Center</span>
            </div> */}
          </div>
          {activeSection === 0 && <PersonalDetails />}
          {activeSection === 1 && <Appearance />}
          {/* {activeSection === 2 && <Privacy />} */}
          {activeSection === 3 && <Security />}
          {/* {activeSection === 4 && <Notifications />} */}
        </div>
      </div>
    </div>
  );
};

export default EditProfile;
