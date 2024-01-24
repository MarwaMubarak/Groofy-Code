import { useState } from "react";
import { GroofyInputProps } from "../../../shared/types";
import "./scss/groofyfield.css";

const GroofyField = (props: GroofyInputProps) => {
  const [fieldValue, setFieldValue] = useState("");
  const handleInputChange = (event: { target: { value: any } }) => {
    try {
      const value = event.target.value;
      setFieldValue(value);
      props.onChange(value);
    } catch (e) {
      console.log(e);
    }
  };
  return (
    <div className="groofy-input">
      <span className="gi-text">{props.giText}</span>
      <input
        type={props.giType}
        placeholder={props.giPlaceholder}
        onChange={handleInputChange}
        value={fieldValue}
      />
    </div>
  );
};

export default GroofyField;
