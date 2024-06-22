package com.groofycode.GroofyCode.dto.Game;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SoloMatchDTO {
    private Long id;
    private Long player1Id;
    private LocalDateTime startTime;
    private String gameType;

    // Constructors
    public SoloMatchDTO() {}

    public SoloMatchDTO(Long id, Long player1Id, LocalDateTime startTime, String gameType) {
        this.id = id;
        this.player1Id = player1Id;
        this.startTime = startTime;
        this.gameType = gameType;
    }
}