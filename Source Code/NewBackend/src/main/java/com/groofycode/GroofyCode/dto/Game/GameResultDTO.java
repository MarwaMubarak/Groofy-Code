package com.groofycode.GroofyCode.dto.Game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResultDTO {
    private String gameType;

    private String gameResult;

    private Integer newRank;

    List<SubmissionDTO> submissionDTOS;
}
