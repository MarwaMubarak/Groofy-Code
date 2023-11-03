import "./scss/testcase.css";
const Testcase = () => {
  return (
    <div className="testcase">
      <div className="card">
        <span className="tc-title">Input</span>
        <pre className="tc-text">
          {"3 2\n1 2 1\n1 2\n1 3"}
          <img className="copy-icn" src="/Assets/SVG/copy.svg" alt="Copy" />
        </pre>
      </div>
      <div className="card">
        <span className="tc-title">Output</span>
        <pre className="tc-text">
          {"3\n6"}
          <img className="copy-icn" src="/Assets/SVG/copy.svg" alt="Copy" />
        </pre>
      </div>
    </div>
  );
};

export default Testcase;
