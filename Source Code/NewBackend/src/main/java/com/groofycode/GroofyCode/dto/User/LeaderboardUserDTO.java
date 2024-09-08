package com.groofycode.GroofyCode.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardUserDTO {
    private Integer rank;
    private String username;
    private Integer rating;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private String country;
}
