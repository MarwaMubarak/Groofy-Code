package com.groofycode.GroofyCode.dto.Game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDTO {
    private String name;
    private Integer solvedCount;
    private String contestId;
    private String index;
    private Integer rating;
}
