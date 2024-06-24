package com.groofycode.GroofyCode.dto.Game;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class SoloMatchDTO {
    private Long id;
    private Long player1Id;
    private LocalDateTime startTime;
    private String gameType;
    String problemURL;
    Object problem;

    // Constructors
    public SoloMatchDTO() {}

    public SoloMatchDTO(Long id, Long player1Id, LocalDateTime startTime, String gameType, Object problem, String problemURL) {
        this.id = id;
        this.player1Id = player1Id;
        this.startTime = startTime;
        this.gameType = gameType;
        this.problem = problem;
        this.problemURL = problemURL;
    }
}