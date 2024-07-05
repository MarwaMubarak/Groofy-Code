import { TcR, GBtn } from "../..";
import classes from "./scss/codingsection.module.css";
import CodeMirror from "@uiw/react-codemirror";
import { vscodeDark } from "@uiw/codemirror-theme-vscode";
import { cpp } from "@codemirror/lang-cpp";
import { python } from "@codemirror/lang-python";
import { javascript } from "@codemirror/lang-javascript";
import { java } from "@codemirror/lang-java";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { gameThunks } from "../../../store/actions";
import { Toast } from "primereact/toast";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";

const languages: any = {
  "c++": {
    ext: cpp(),
    val: `#include <iostream>\n\nusing namespace std;\n\nint main() {\n\tcout << "Hello world!";\n}\n`,
  },
  js: { ext: javascript(), val: `console.log("Hello world!");\n` },
  jar: {
    ext: java(),
    val: `public class Main {\n\tpublic static void main(String[] args) {\n\t\tSystem.out.println("Hello world!");\n\t}\n}\n`,
  },
  py: { ext: python(), val: `print("Hello world!")\n` },
};

const CodingSection = () => {
  const [currentLang, setCurrentLang] = useState("c++");
  const [currentCode, setCurrentCode] = useState(languages[currentLang].val);
  const dispatch = useDispatch();
  const isSubmitting = useSelector((state: any) => state.submission.isLoading);
  const submitState = useSelector((state: any) => state.submission.submitState);
  const toast = useRef<Toast>(null);
  const problemURL = useSelector((state: any) => state.game.problemUrl);
  const gameID = useSelector((state: any) => state.game.gameID);
  const submissions = useSelector((state: any) => state.submission.submissions);

  useEffect(() => {
    if (gameID) {
      dispatch(gameThunks.getGameSubmissions(gameID) as any);
    }
  }, [dispatch, gameID]);

  const submitCode = async () => {
    return await dispatch(
      gameThunks.submitCode({
        code: currentCode,
        language: "GNU G++17 7.3.0",
        problemUrl: problemURL,
        gameID: gameID,
      }) as any
    );
  };

  return (
    <div className={classes.esec}>
      <Toast ref={toast} style={{ padding: "0.75rem" }} />
      <div className={classes.coding_sec}>
        <div className={classes.lng}>
          <span className={classes.cl_title}>Language:</span>
          <select
            value={currentLang}
            onChange={(e) => setCurrentLang(e.target.value)}
          >
            <option disabled>Select a Language</option>
            <option value="c++">C++</option>
            <option value="js">JavaScript</option>
            <option value="jar">Java</option>
            <option value="py">Python</option>
          </select>
        </div>
        <div className={classes.editor}>
          <CodeMirror
            extensions={[languages[currentLang].ext]}
            theme={vscodeDark}
            value={currentCode}
            onChange={(val) => setCurrentCode(val)}
            height="400px"
            style={{ fontSize: "16px" }}
          />
        </div>
        <div className={classes.compiling_btns}>
          {/* <GBtn
            btnText="Run Testcases"
            icnSrc="/Assets/SVG/run-testcases.svg"
            clickEvent={() => {}}
          /> */}
          <GBtn
            btnText={submitState !== "" ? submitState : "Submit"}
            icnSrc="/Assets/SVG/submit.svg"
            btnState={isSubmitting}
            clickEvent={() => {
              submitCode()
                .then((res: any) => {
                  if (res.message !== "Match") {
                    toast.current?.show({
                      severity: res.body === "Accepted" ? "success" : "error",
                      summary: res.status,
                      detail: res.body,
                      life: 3000,
                    });
                  }
                })
                .catch((err: any) => {
                  toast.current?.show({
                    severity: "error",
                    summary: err.status,
                    detail: err.message,
                    life: 3000,
                  });
                });
            }}
          />
        </div>
      </div>
      <DataTable
        value={submissions}
        tableStyle={{
          minWidth: "50rem",
          marginTop: "30px",
        }}
        paginator
        rows={5}
        paginatorTemplate={
          "FirstPageLink PrevPageLink CurrentPageReport NextPageLink LastPageLink RowsPerPageDropdown"
        }
        emptyMessage="No submissions found"
        paginatorClassName={classes.paginator}
      >
        <Column
          style={{ backgroundColor: "#303031", background: "#303031" }}
          field="submissionTime"
          header="Submission Time"
        ></Column>
        <Column
          style={{ backgroundColor: "#303031", background: "#303031" }}
          field="verdict"
          header="Verdict"
          body={(rowData: any) => {
            return (
              <span
                style={{
                  color:
                    rowData.verdict === "Accepted"
                      ? "green"
                      : rowData.verdict === "Wrong Answer"
                      ? "#f55353"
                      : "yellow",
                }}
              >
                {rowData.verdict}
              </span>
            );
          }}
        ></Column>
        <Column
          style={{ backgroundColor: "#303031", background: "#303031" }}
          field="language"
          header="Language"
        ></Column>
      </DataTable>

      {/* <div className={classes.op_sec}>
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
      </div> */}
    </div>
  );
};

export default CodingSection;
