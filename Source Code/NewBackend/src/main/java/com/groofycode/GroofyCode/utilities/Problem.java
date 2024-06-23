package com.groofycode.GroofyCode.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    private String name;
    private Integer solvedCount;
    private String contestId;
    private String index;
}
