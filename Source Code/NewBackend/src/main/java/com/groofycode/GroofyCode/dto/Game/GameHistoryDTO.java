package com.groofycode.GroofyCode.dto.Game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameHistoryDTO {
    private String gameType;
    private String gameResult;
    private LocalDateTime gameDate;
    private String ratingChange;
    private String newRating;
}
