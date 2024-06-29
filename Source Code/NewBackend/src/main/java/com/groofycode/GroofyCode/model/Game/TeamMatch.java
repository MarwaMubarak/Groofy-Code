package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.Team.TeamModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@DiscriminatorValue("TEAM")
public class TeamMatch extends Game {

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private TeamModel team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private TeamModel team2;

    private String problemUrl2;

    private String problemUrl3;

    public TeamMatch() {
        setGameType(GameType.TEAM);
    }

    public TeamMatch(TeamModel team1, TeamModel team2, String problemUrl, String problemUrl2, String problemUrl3, LocalDateTime startTime, LocalDateTime endTime, double duration) {
        this.team1 = team1;
        this.team2 = team2;
        setProblemUrl(problemUrl);
        setStartTime(startTime);
        setEndTime(endTime);
        setDuration(duration);
        setGameStatus(GameStatus.ONGOING);
        setGameType(GameType.TEAM);
        this.problemUrl2 = problemUrl2;
        this.problemUrl3 = problemUrl3;
    }

}
