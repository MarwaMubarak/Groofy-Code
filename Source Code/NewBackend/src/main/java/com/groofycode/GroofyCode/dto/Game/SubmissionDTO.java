package com.groofycode.GroofyCode.dto.Game;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubmissionDTO {
    private LocalDateTime submissionTime;

    private String language;

    private String verdict;
}
