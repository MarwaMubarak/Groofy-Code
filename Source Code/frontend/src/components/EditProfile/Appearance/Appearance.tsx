import React, { useState } from "react";
import classes from "./scss/appearance.module.css";
import GBtn from "../../GBtn/GBtn";
import { classNames } from "primereact/utils";
import { Button } from "primereact/button";
import { ColorPicker, ColorPickerChangeEvent } from "primereact/colorpicker";
import { useDispatch, useSelector } from "react-redux";
import { userThunks } from "../../../store/actions";

const Appearance = () => {
  const user = useSelector((state: any) => state.auth.user);
  const [color, setColor] = useState<string>(user.accountColor);
  const dispatch = useDispatch();
  const changeBackgroundColor = async (e: any) => {
    e.preventDefault();
    dispatch(userThunks.changeBackgroundColor("#" + color) as any);
  };
  return (
    <div className={classes.edit_info}>
      <h3 className={classes.edit_header}>Appearance & Style</h3>
      <form className={classes.edit_content} onSubmit={changeBackgroundColor}>
        <div className={classes.color_section}>
          <span>Change your background color</span>
          <ColorPicker value={color} onChange={(e: any) => setColor(e.value)} />
        </div>
        <Button className={classNames(classes.groofybtn)} label="Save" />
      </form>
    </div>
  );
};

export default Appearance;
