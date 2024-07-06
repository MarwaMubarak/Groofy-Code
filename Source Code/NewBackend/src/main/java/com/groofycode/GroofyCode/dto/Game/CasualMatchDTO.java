package com.groofycode.GroofyCode.dto.Game;


import com.groofycode.GroofyCode.model.Game.CasualMatch;
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
public class CasualMatchDTO extends GameDTO {
    private List<Long> players2Ids;

    public CasualMatchDTO(CasualMatch casualMatch, Object problemStatement) {
        super(
                casualMatch.getId(),
                casualMatch.getPlayers1().stream().map(UserModel::getId).collect(Collectors.toList()),
                casualMatch.getPlayers2().stream().map(UserModel::getId).collect(Collectors.toList()),
                casualMatch.getProblemUrl(),
                casualMatch.getStartTime(),
                casualMatch.getEndTime(),
                casualMatch.getDuration(),
                "Casual",
                casualMatch.getGameStatus().toString(),
                null // assgin it later
                , null, null
        );
        setProblemStatement(problemStatement);
    }
}
