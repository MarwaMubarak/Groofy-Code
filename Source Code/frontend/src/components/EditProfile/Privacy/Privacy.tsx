import React from "react";
import "./scss/privacy.css";
import GBtn from "../../GBtn/GBtn";
import { Button } from "primereact/button";

const Privacy = () => {
  return (
    <div className="edit-info">
      <h3 className="edit-header">Privacy</h3>
      <div className="edit-content"></div>
      <GBtn btnText="Save Changes" clickEvent={() => {}} />
      <Button label="Click" className="ggb" />
    </div>
  );
};

export default Privacy;
