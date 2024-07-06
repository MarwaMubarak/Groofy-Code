import { useDispatch, useSelector } from "react-redux";
import { Testcase } from "../..";
import classes from "./scss/problemsection.module.css";
import { useContext, useEffect, useRef, useState } from "react";
import { MathJax } from "better-react-mathjax";
import { MathJaxBaseContext } from "better-react-mathjax";
import { gameThunks } from "../../../store/actions";

const ProblemSection = () => {
  const dispatch = useDispatch();
  const problem1 = useSelector((state: any) => state.game.problemStatement);
  const problem2 = useSelector((state: any) => state.game.problemStatement2);
  const problem3 = useSelector((state: any) => state.game.problemStatement3);
  const problems1ID = useSelector((state: any) => state.game.problems1ID);

  const [currentProblem, setCurrentProblem] = useState(problem1);

  const gameType = useSelector((state: any) => state.game.gameType);
  const [problemIdx, setProblemIdx] = useState(1);

  console.log("Idx:", problemIdx);

  console.log("Problem Ids:", problems1ID);

  const changeCurrentProblem = (idx: any) => {
    dispatch(gameThunks.changeSelectedProblem(idx) as any);
    setProblemIdx(idx);
    if (idx === 1) {
      setCurrentProblem(problem1);
    } else if (idx === 2) {
      setCurrentProblem(problem2);
    } else {
      setCurrentProblem(problem3);
    }
  };

  return (
    <div className={classes.psec}>
      {gameType === "Team" && (
        <div className={classes.problem_tabs} style={{ marginBottom: "20px" }}>
          <span>Problems</span>
          <div className={classes.tabs}>
            <div
              className={`${classes.tab} ${
                problemIdx === 1 && classes.active
              } ${
                problems1ID.at(problemIdx - 1) === true ? classes.accepted : ""
              }`}
              onClick={() => {
                changeCurrentProblem(1);
              }}
            >
              1
            </div>
            <div
              className={`${classes.tab} ${problemIdx === 2 && classes.active}`}
              onClick={() => {
                changeCurrentProblem(2);
              }}
            >
              2
            </div>
            <div
              className={`${classes.tab} ${problemIdx === 3 && classes.active}`}
              onClick={() => {
                changeCurrentProblem(3);
              }}
            >
              3
            </div>
          </div>
        </div>
      )}

      <div className={classes.problem_disc} style={{ marginBottom: "20px" }}>
        <span className={classes.ptitle}>
          {currentProblem?.header[0].substring(3)}
        </span>
        <span className={classes.complexity}>
          {currentProblem?.header.map((item: any, idx: number) => {
            if (idx !== 0) {
              return <p key={idx}>{item}</p>;
            }
            return null;
          })}
        </span>
        <p className={classes.pdisc}>
          {currentProblem?.statement.map((row: any, idx: any) => {
            if (row.startsWith("IMAGELINKGROOFYCODE")) {
              return (
                <MathJax key={idx}>
                  <div
                    style={{
                      margin: "10px auto",
                      width: "100%",
                      textAlign: "center",
                    }}
                  >
                    <img src={row.substring(20)} alt="ProblemPhoto" />
                  </div>
                </MathJax>
              );
            }
            return (
              <MathJax key={idx}>
                <div
                  dangerouslySetInnerHTML={{ __html: row }}
                  style={{ lineHeight: "1.8", color: "white" }}
                ></div>
              </MathJax>
            );
          })}
        </p>
      </div>
      {currentProblem?.input.length > 0 && (
        <div className={classes.problem_disc} style={{ marginBottom: "20px" }}>
          <span className={classes.ptitle}>{currentProblem?.input[0]}</span>
          <p className={classes.pdisc}>
            {currentProblem?.input.map((row: any, idx: number) => {
              if (idx !== 0) {
                return (
                  <MathJax key={idx}>
                    <div
                      dangerouslySetInnerHTML={{ __html: row }}
                      style={{ lineHeight: "1.8" }}
                    ></div>
                  </MathJax>
                );
              }
              return null;
            })}
          </p>
        </div>
      )}

      {currentProblem?.output.length > 0 && (
        <div className={classes.problem_disc}>
          <span className={classes.ptitle}>{currentProblem?.output[0]}</span>
          <p className={classes.pdisc}>
            {currentProblem?.output.map((row: any, idx: number) => {
              if (idx !== 0) {
                return (
                  <MathJax key={idx}>
                    <div
                      dangerouslySetInnerHTML={{ __html: row }}
                      style={{ lineHeight: "1.8", color: "white" }}
                    ></div>
                  </MathJax>
                );
              }
              return null;
            })}
          </p>
        </div>
      )}
      {currentProblem?.sampleTests.length > 0 && (
        <div className={classes.testcases}>
          <span className={classes.tch}>Testcases</span>
          <div className={classes.tcs_container}>
            {currentProblem?.sampleTests.map((tc: any, idx: number) => (
              <div className={classes.tc_box} key={idx}>
                <span className={classes.tc_num}>Testcase {idx + 1}:</span>
                <Testcase input={tc[0]} output={tc[1]} />
              </div>
            ))}
          </div>
        </div>
      )}

      {currentProblem?.notes.length > 0 && (
        <div className={classes.problem_disc}>
          <span className={classes.ptitle}>{currentProblem?.notes[0]}</span>
          <p className={classes.pdisc}>
            {currentProblem?.notes.map((row: any, idx: number) => {
              if (idx !== 0) {
                return (
                  <MathJax key={idx}>
                    <div
                      dangerouslySetInnerHTML={{ __html: row }}
                      style={{ lineHeight: "1.8", color: "white" }}
                    ></div>
                  </MathJax>
                );
              }
              return null;
            })}
          </p>
        </div>
      )}
    </div>
  );
};

export default ProblemSection;
