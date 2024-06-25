package com.groofycode.GroofyCode.dto.Game;


import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.User.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long id;
    private List<Long> players1Ids;
    private List<Long> players2Ids;
    private String problemUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double duration;
    private String gameType;
    private String gameStatus;
    private Object problemStatement;

    // Custom constructor to map from Game entity
    public GameDTO(Game game) {
        this.id = game.getId();
        this.players1Ids = game.getPlayers1().stream().map(UserModel::getId).collect(Collectors.toList());
        this.players2Ids = game.getPlayers2().stream().map(UserModel::getId).collect(Collectors.toList());
        this.problemUrl = game.getProblemUrl();
        this.startTime = game.getStartTime();
        this.endTime = game.getEndTime();
        this.duration = game.getDuration();
        this.gameType = game.getGameType() != null ? game.getGameType().toString() : null;
        this.gameStatus = game.getGameStatus() != null ? game.getGameStatus().toString() : null;
    }
}
