import "./scss/problemsbar.css";
const ProblemsBar = () => {
  return (
    <div className="problems-bar">
      <span>Problems</span>
      <div className="problems-container">
        <div className="problem y active">1</div>
        <div className="problem n">2</div>
        <div className="problem p">3</div>
      </div>
    </div>
  );
};

export default ProblemsBar;
