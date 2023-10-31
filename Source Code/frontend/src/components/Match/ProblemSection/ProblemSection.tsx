import { Testcase } from "../..";
import "./scss/problemsection.css";
const ProblemSection = () => {
  return (
    <div className="psec">
      {/* <ProblemsBar /> */}
      <div className="problem-disc">
        <span className="ptitle">Problem Title</span>
        <p className="pdisc">
          Lorem ipsum, dolor sit amet consectetur adipisicing elit. Illum maxime
          quas dolorem optio deserunt obcaecati, quam natus ipsa vitae amet ad
          reiciendis modi perferendis tenetur. Doloribus illo laborum deleniti
          cumque. Praesentium sit tempora consequatur modi? Ratione cupiditate
          rerum repudiandae obcaecati, distinctio laborum laudantium? Sapiente
          facere, maiores, inventore iusto exercitationem ab mollitia eos quasi
          quas dolorem dolor, omnis aut vero? Nemo. Assumenda eos ipsam quis
          iure nemo provident maiores ex eius maxime aspernatur voluptatem sunt,
          fugit quod perferendis sit. Ab facere repellat provident vero placeat
          in aliquid, ad molestiae reprehenderit quam!
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
  );
};

export default ProblemSection;
