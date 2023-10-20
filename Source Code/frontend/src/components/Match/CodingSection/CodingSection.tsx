import { TcR, GBtn } from "../..";
import "./scss/codingsection.css";
const CodingSection = () => {
  return (
    <div className="esec">
      <div className="coding-sec">
        <div className="lng">
          <span className="cl-title">Language:</span>
          <select value={"c++"}>
            <option disabled>Select a Language</option>
            <option value="c++">C++</option>
            <option value="c">C</option>
            <option value="js">JavaScript</option>
            <option value="jar">Java</option>
            <option value="py">Python</option>
          </select>
        </div>
        <div className="editor">
          <textarea></textarea>
        </div>
        <div className="compiling-btns">
          <GBtn
            btnText="Run Testcases"
            icnSrc="/Assets/run-testcases.svg"
            clickEvent={() => {}}
          />
          <GBtn
            btnText="Submit"
            icnSrc="/Assets/submit.svg"
            clickEvent={() => {}}
          />
        </div>
      </div>
      <div className="op-sec">
        <span className="op-title">Output:</span>
        <div className="op-info">
          <div className="op-box">
            <span className="info-title">Valid Testcases:</span>
            <div className="op-info-container">
              <TcR tcNum={1} icnSrc="/Assets/success.png" tcStatus />
              <TcR tcNum={2} icnSrc="/Assets/success.png" tcStatus />
            </div>
          </div>
          <div className="op-box">
            <span className="info-title">Invalid Testcases:</span>
            <div className="op-info-container">
              <TcR tcNum={3} icnSrc="/Assets/wrong.png" tcStatus={false} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CodingSection;
