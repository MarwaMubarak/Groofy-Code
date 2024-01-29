import { ChangeEvent, useRef, useState } from "react";
import {
  Appearance,
  GBtn,
  GroofyField,
  Notifications,
  PersonalDetails,
  Privacy,
  Security,
  SideBar,
} from "../../components";
import "./scss/editprofile.css";
import { Dropdown, DropdownChangeEvent } from "primereact/dropdown";
import { userSchema } from "../../shared/schemas";
import { useFormik } from "formik";
import { Toast } from "primereact/toast";
import { useSelector } from "react-redux";
import { Button } from "primereact/button";

const EditProfile = () => {
  const toast = useRef<Toast>(null);
  const [activeSection, setActiveSection] = useState(0);
  return (
    <div className="editprofile-container">
      <Toast ref={toast} />
      <SideBar idx={1} />
      <div className="editprofile-page">
        <div className="edtiprofile-settings">
          <i className="pi pi-cog" />
          <h1>Settings</h1>
        </div>
        <div className="edit-profile">
          <div className="edit-menu">
            <div
              className={`edit-section ${activeSection === 0 ? "active" : ""}`}
              onClick={() => setActiveSection(0)}
            >
              <div className="es-img">
                <i className="pi pi-id-card" />
              </div>
              <span className="es-title">Personal Details</span>
            </div>
            <div
              className={`edit-section ${activeSection === 1 ? "active" : ""}`}
              onClick={() => setActiveSection(1)}
            >
              <div className="es-img">
                <i className="pi pi-palette" />
              </div>
              <span className="es-title">Appearance & Style</span>
            </div>
            <div
              className={`edit-section ${activeSection === 2 ? "active" : ""}`}
              onClick={() => setActiveSection(2)}
            >
              <div className="es-img">
                <i className="pi pi-lock" />
              </div>
              <span className="es-title">Privacy Preferences</span>
            </div>
            <div
              className={`edit-section ${activeSection === 3 ? "active" : ""}`}
              onClick={() => setActiveSection(3)}
            >
              <div className="es-img">
                <i className="pi pi-shield" />
              </div>
              <span className="es-title">Security Settings</span>
            </div>
            <div
              className={`edit-section ${activeSection === 4 ? "active" : ""}`}
              onClick={() => setActiveSection(4)}
            >
              <div className="es-img">
                <i className="pi pi-bell" />
              </div>
              <span className="es-title">Notification Center</span>
            </div>
          </div>
          {activeSection === 0 && <PersonalDetails />}
          {activeSection === 1 && <Appearance />}
          {activeSection === 2 && <Privacy />}
          {activeSection === 3 && <Security />}
          {activeSection === 4 && <Notifications />}
        </div>
      </div>
    </div>
  );
};

export default EditProfile;
