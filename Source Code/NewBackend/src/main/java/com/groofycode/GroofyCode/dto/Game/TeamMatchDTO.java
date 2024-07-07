package com.groofycode.GroofyCode.dto.Game;

import com.groofycode.GroofyCode.model.Game.TeamMatch;
import com.groofycode.GroofyCode.model.Team.TeamMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TeamMatchDTO extends GameDTO {

    private Long team1Id;
    private Long team2Id;
    private Object problemStatement2;
    private Object problemStatement3;
    private String problemUrl2;
    private String problemUrl3;

    public TeamMatchDTO(TeamMatch teamMatch, Object problemStatement,Object problemStatement2,Object problemStatement3, String problemUrl2, String problemUrl3) {
        super(
                teamMatch.getId(),
                teamMatch.getTeam1().getMembers().stream()
                        .map(teamMember -> teamMember.getUser().getId())
                        .collect(Collectors.toList()),
                teamMatch.getTeam2().getMembers().stream()
                        .map(teamMember -> teamMember.getUser().getId())
                        .collect(Collectors.toList()),
                teamMatch.getProblemUrl(),
                teamMatch.getStartTime(),
                teamMatch.getEndTime(),
                teamMatch.getDuration(),
                "Team",
                teamMatch.getGameStatus().toString(),
                null // problemStatement will be assigned later
                , null, null
        );
        this.team1Id = teamMatch.getTeam1().getId();
        this.team2Id = teamMatch.getTeam2().getId();
        setProblemStatement(problemStatement);
        this.problemStatement2 = problemStatement2;
        this.problemStatement3 = problemStatement3;
        this.problemUrl2 = problemUrl2;
        this.problemUrl3 = problemUrl3;
    }
}
