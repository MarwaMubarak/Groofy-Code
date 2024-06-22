package com.groofycode.GroofyCode.dto.Game;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
public class RankedMatchDTO {
    private Long id;
    private Long player1Id;
    private Long player2Id;
    private LocalDateTime startTime;
    private String gameType;

    // Constructors
    public RankedMatchDTO() {}

    public RankedMatchDTO(Long id, Long player1Id, Long player2Id, LocalDateTime startTime, String gameType) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.startTime = startTime;
        this.gameType = gameType;
    }


}