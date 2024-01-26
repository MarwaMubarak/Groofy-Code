import { useState } from "react";

const useInput = (validateValue: (value: any) => Boolean) => {
  const [enteredValue, setEnteredValue] = useState("");
  const [isTouched, setIsTouched] = useState(false);
  const valueIsValid = validateValue(enteredValue);
  const hasError = !valueIsValid && isTouched;
  const ChangeHandler = (event: any) => {
    setEnteredValue(event.target.value);
  };
  const BlurHandler = (event: any) => {
    setIsTouched(true);
  };
  const Reset = () => {
    setEnteredValue("");
    setIsTouched(false);
  };
  return {
    value: enteredValue,
    isValid: valueIsValid,
    hasError,
    ChangeHandler,
    BlurHandler,
    Reset,
  };
};

export default useInput;
