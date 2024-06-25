package com.groofycode.GroofyCode.dto.Game;


import com.groofycode.GroofyCode.model.Game.SoloMatch;
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
public class SoloMatchDTO extends GameDTO {
    // Additional fields for SoloMatch can be added here if any

    public SoloMatchDTO(SoloMatch soloMatch, Object problemStatement) {
        super(
                soloMatch.getId(),
                soloMatch.getPlayers1().stream().map(UserModel::getId).collect(Collectors.toList()),
                null, // SoloMatch doesn't have players2
                soloMatch.getProblemUrl(),
                soloMatch.getStartTime(),
                soloMatch.getEndTime(),
                soloMatch.getDuration(),
                soloMatch.getGameType().toString(),
                soloMatch.getGameStatus().toString(),
                null // assgin it later
        );
        setProblemStatement(problemStatement);
    }
}
