import { Testcase } from "../..";
import classes from "./scss/problemsection.module.css";

const ProblemSection = () => {
  return (
    <div className={classes.psec}>
      {/* <ProblemsBar /> */}
      <div className={classes.problem_disc}>
        <span className={classes.ptitle}>Problem Title</span>
        <p className={classes.pdisc}>
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
      <div className={classes.testcases}>
        <span className={classes.tch}>Testcases</span>
        <div className={classes.tcs_container}>
          {[1, 2, 3].map((i) => (
            <div className={classes.tc_box} key={i}>
              <span className={classes.tc_num}>Testcase {i}:</span>
              <Testcase />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProblemSection;
