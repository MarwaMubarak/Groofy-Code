import { Button } from "primereact/button";
import classes from "./scss/team-card.module.css";
import { TeamCardProps } from "../../shared/types";
import { Link, useNavigate } from "react-router-dom";

const TeamCard = (props: TeamCardProps) => {
  const navigate = useNavigate();
  return (
    <div className={classes.team_card}>
      <div className={classes.team_info}>
        <h3>Team Name</h3>
        <div className={classes.team_tags}>
          <span>Members: {props.membersCount}/3</span>
          <span>
            Team Creator:{" "}
            <Link to={`/profile/${props.creatorUsername}`}>
              {props.creatorUsername}
            </Link>
          </span>
        </div>
      </div>
      <div className={classes.team_actions}>
        <Button
          label="View"
          icon="bi bi-eye-fill"
          className={`${classes.cr_btn} ${classes.view}`}
          onClick={() => navigate(`/teams/${props.name}`)}
        />
      </div>
    </div>
  );
};

export default TeamCard;
