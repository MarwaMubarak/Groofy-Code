import {
  MatchHeader,
  ProblemsBar,
  Scoreboard,
  Testcase,
} from "../../components";
import "./scss/match.css";

const Match = () => {
  return (
    <>
      <MatchHeader />
      <div className="match-div">
        <Scoreboard />
        <div className="match-sections">
          <div className="psec">
            <ProblemsBar />
            <div className="problem-disc">
              <span className="ptitle">Problem Title</span>
              <p className="pdisc">
                Lorem ipsum, dolor sit amet consectetur adipisicing elit. Illum
                maxime quas dolorem optio deserunt obcaecati, quam natus ipsa
                vitae amet ad reiciendis modi perferendis tenetur. Doloribus
                illo laborum deleniti cumque. Praesentium sit tempora
                consequatur modi? Ratione cupiditate rerum repudiandae
                obcaecati, distinctio laborum laudantium? Sapiente facere,
                maiores, inventore iusto exercitationem ab mollitia eos quasi
                quas dolorem dolor, omnis aut vero? Nemo. Assumenda eos ipsam
                quis iure nemo provident maiores ex eius maxime aspernatur
                voluptatem sunt, fugit quod perferendis sit. Ab facere repellat
                provident vero placeat in aliquid, ad molestiae reprehenderit
                quam!
              </p>
            </div>
            <div className="testcases">
              <span className="tch">Testcases</span>
              <div className="tcs-container">
                {[1, 2, 3].map((i) => (
                  <div className="tc-box">
                    <span className="tc-num">Testcase {i}:</span>
                    <Testcase />
                  </div>
                ))}
              </div>
            </div>
          </div>
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
                <button>
                  <img
                    className="run-tc-icn"
                    src="/Assets/run-testcases.svg"
                    alt="Run"
                  />
                  Run Testcases
                </button>
                <button>
                  <img
                    className="submit-icn"
                    src="/Assets/submit.svg"
                    alt="Submit"
                  />
                  Submit
                </button>
              </div>
            </div>
            <div className="op-sec">
              <span className="op-title">Output:</span>
              <div className="op-info">
                <div className="op-box">
                  <span className="info-title">Valid Testcases:</span>
                  <div className="op-info-container">
                    <div className="op-info-box">
                      <img src="/Assets/success.png" alt="Success" />
                      Testcase 1
                    </div>
                    <div className="op-info-box">
                      <img src="/Assets/success.png" alt="Success" />
                      Testcase 2
                    </div>
                  </div>
                </div>
                <div className="op-box">
                  <span className="info-title">Invalid Testcases:</span>
                  <div className="op-info-container">
                    <div className="op-info-box">
                      <img src="/Assets/wrong.png" alt="Wrong" />
                      Testcase 3<span className="details">Details</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Match;
