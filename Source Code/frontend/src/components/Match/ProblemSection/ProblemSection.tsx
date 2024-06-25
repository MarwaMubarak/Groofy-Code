import { useSelector } from "react-redux";
import { Testcase } from "../..";
import classes from "./scss/problemsection.module.css";
import { useContext, useEffect, useRef } from "react";
import { MathJax } from "better-react-mathjax";
import { MathJaxBaseContext } from "better-react-mathjax";

const ProblemSection = () => {
  const problem = useSelector((state: any) => state.game.problemStatement);
  const user = useSelector((state: any) => state.auth.user);
  const mathJax = useContext(MathJaxBaseContext);
  const mathElementRef = useRef(null);

  useEffect(() => {
    if (mathJax && mathElementRef.current) {
      mathJax.promise.then((MathJaxObject: any) => {
        MathJaxObject.typesetPromise([mathElementRef.current]);
      });
    }

    return () => {
      // Cleanup: Tell MathJax to stop processing if the element is removed
      if (mathJax && mathElementRef.current) {
        mathJax.promise.then((MathJaxObject: any) => {
          MathJaxObject.typesetPromise([mathElementRef.current]);
        });
      }
    };
  }, [mathJax]);

  console.log(problem);

  return (
    <div className={classes.psec}>
      <div className={classes.problem_disc}>
        <span className={classes.ptitle}>
          {problem?.header[0].substring(3)}
        </span>
        <span className={classes.complexity}>
          {problem?.header.map((item: any, idx: number) => {
            if (idx !== 0) {
              return <p>{item}</p>;
            }
            return null;
          })}
        </span>
        <p className={classes.pdisc}>
          {problem?.statement.map((row: any) => {
            if (row.startsWith("IMAGELINKGROOFYCODE")) {
              return (
                <div
                  style={{
                    margin: "10px auto",
                    width: "100%",
                    textAlign: "center",
                  }}
                >
                  <img src={row.substring(20)} alt="ProblemPhoto" />
                </div>
              );
            }
            return (
              <div
                dangerouslySetInnerHTML={{ __html: row }}
                ref={mathElementRef}
                style={{ lineHeight: "1.8", color: "white" }}
              ></div>
            );
          })}
        </p>
      </div>
      {problem?.input.length > 0 && (
        <div className={classes.problem_disc}>
          <span className={classes.ptitle}>{problem?.input[0]}</span>
          <p className={classes.pdisc}>
            {problem?.input.map((row: any, idx: number) => {
              if (idx !== 0) {
                return (
                  <div
                    dangerouslySetInnerHTML={{ __html: row }}
                    ref={mathElementRef}
                    style={{ lineHeight: "1.8" }}
                  ></div>
                );
              }
              return null;
            })}
          </p>
        </div>
      )}

      {problem?.output.length > 0 && (
        <div className={classes.problem_disc}>
          <span className={classes.ptitle}>{problem?.output[0]}</span>
          <p className={classes.pdisc}>
            {problem?.output.map((row: any, idx: number) => {
              if (idx !== 0) {
                return (
                  <div
                    dangerouslySetInnerHTML={{ __html: row }}
                    ref={mathElementRef}
                    style={{ lineHeight: "1.8", color: "white" }}
                  ></div>
                );
              }
              return null;
            })}
          </p>
        </div>
      )}
      {problem?.sampleTests.length > 0 && (
        <div className={classes.testcases}>
          <span className={classes.tch}>Testcases</span>
          <div className={classes.tcs_container}>
            {problem?.sampleTests.map((tc: any, idx: number) => (
              <div className={classes.tc_box} key={idx}>
                <span className={classes.tc_num}>Testcase {idx + 1}:</span>
                <Testcase input={tc[0]} output={tc[1]} />
              </div>
            ))}
          </div>
        </div>
      )}

      {problem?.notes.length > 0 && (
        <div className={classes.problem_disc}>
          <span className={classes.ptitle}>{problem?.notes[0]}</span>
          <p className={classes.pdisc}>
            {problem?.notes.map((row: any, idx: number) => {
              if (idx !== 0) {
                return (
                  <div
                    dangerouslySetInnerHTML={{ __html: row }}
                    ref={mathElementRef}
                    style={{ lineHeight: "1.8", color: "white" }}
                  ></div>
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
