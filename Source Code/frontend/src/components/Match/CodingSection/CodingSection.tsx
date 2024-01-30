import { TcR, GBtn } from "../..";
import classes from "./scss/codingsection.module.css";

const CodingSection = () => {
  return (
    <div className={classes.esec}>
      <div className={classes.coding_sec}>
        <div className={classes.lng}>
          <span className={classes.cl_title}>Language:</span>
          <select value={"c++"}>
            <option disabled>Select a Language</option>
            <option value="c++">C++</option>
            <option value="c">C</option>
            <option value="js">JavaScript</option>
            <option value="jar">Java</option>
            <option value="py">Python</option>
          </select>
        </div>
        <div className={classes.editor}>
          <textarea></textarea>
        </div>
        <div className={classes.compiling_btns}>
          <GBtn
            btnText="Run Testcases"
            icnSrc="/Assets/SVG/run-testcases.svg"
            clickEvent={() => {}}
          />
          <GBtn
            btnText="Submit"
            icnSrc="/Assets/SVG/submit.svg"
            clickEvent={() => {}}
          />
        </div>
      </div>
      <div className={classes.op_sec}>
        <span className={classes.op_title}>Output:</span>
        <div className={classes.op_info}>
          <div className={classes.op_box}>
            <span className={classes.info_title}>Valid Testcases:</span>
            <div className={classes.op_info_container}>
              <TcR tcNum={1} icnSrc="/Assets/Images/success.png" tcStatus />
              <TcR tcNum={2} icnSrc="/Assets/Images/success.png" tcStatus />
            </div>
          </div>
          <div className={classes.op_box}>
            <span className={classes.info_title}>Invalid Testcases:</span>
            <div className={classes.op_info_container}>
              <TcR
                tcNum={3}
                icnSrc="/Assets/Images/wrong.png"
                tcStatus={false}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CodingSection;
