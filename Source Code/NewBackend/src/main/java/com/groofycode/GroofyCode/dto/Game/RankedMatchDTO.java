package com.groofycode.GroofyCode.dto.Game;


import com.groofycode.GroofyCode.model.Game.RankedMatch;
import com.groofycode.GroofyCode.model.User.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RankedMatchDTO extends GameDTO {

    public RankedMatchDTO(RankedMatch rankedMatch, Object problemStatement) {
        super(
                rankedMatch.getId(),
                rankedMatch.getPlayers1().stream().map(UserModel::getId).collect(Collectors.toList()),
                rankedMatch.getPlayers2().stream().map(UserModel::getId).collect(Collectors.toList()),
                rankedMatch.getProblemUrl(),
                rankedMatch.getStartTime(),
                rankedMatch.getEndTime(),
                rankedMatch.getDuration(),
                "Ranked",
                rankedMatch.getGameStatus().toString(),
                null // assgin it later
                , null, null
        );
        setProblemStatement(problemStatement);
    }
}
