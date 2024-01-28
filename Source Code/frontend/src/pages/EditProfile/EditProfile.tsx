import { useState } from "react";
import { GBtn, GroofyField } from "../../components";
import "./scss/editprofile.css";
import { Dropdown, DropdownChangeEvent } from "primereact/dropdown";

interface Country {
  name: string;
  code: string;
}

const EditProfile = () => {
  const [selectedCountry, setSelectedCountry] = useState<Country | null>(null);
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
  return (
    <div className="editprofile-container">
      <div className="edit-profile">
        <div className="edit-menu">
          <div className="edit-section active">
            <div className="es-img">
              <img src="/Assets/SVG/ProfileIcon.svg" alt="ProfileIcon" />
            </div>
            <span className="es-title">Personal Details</span>
          </div>
          <div className="edit-section">
            <div className="es-img">
              <img src="/Assets/SVG/ThemeIcon.svg" alt="ProfileIcon" />
            </div>
            <span className="es-title">Appearance & Style</span>
          </div>
          <div className="edit-section">
            <div className="es-img">
              <img src="/Assets/SVG/PrivacyIcon.svg" alt="ProfileIcon" />
            </div>
            <span className="es-title">Privacy Preferences</span>
          </div>
          <div className="edit-section">
            <div className="es-img">
              <img src="/Assets/SVG/SecurityIcon.svg" alt="ProfileIcon" />
            </div>
            <span className="es-title">Security Settings</span>
          </div>
          <div className="edit-section">
            <div className="es-img">
              <img src="/Assets/SVG/NotifyBlue.svg" alt="ProfileIcon" />
            </div>
            <span className="es-title">Notification Center</span>
          </div>
        </div>
        <div className="edit-info">
          <h3 className="edit-header">Profile</h3>
          <form className="edit-content">
            <GroofyField
              giText="Firstname"
              giValue=""
              giPlaceholder="Enter your firstname"
              giType="text"
              errState={false}
              onChange={() => {}}
              onBlur={() => {}}
            />
            <GroofyField
              giText="Lastname"
              giValue=""
              giPlaceholder="Enter your lastname"
              giType="text"
              errState={false}
              onChange={() => {}}
              onBlur={() => {}}
            />
            <div className="country-selector">
              <div className="cs-title">Select your country:</div>
              <Dropdown
                value={selectedCountry}
                onChange={(e: DropdownChangeEvent) =>
                  setSelectedCountry(e.value)
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
              btnText="Save your information"
              btnType={true}
              clickEvent={() => {}}
            />
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditProfile;
