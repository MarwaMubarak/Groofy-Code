package com.groofycode.GroofyCode.dto.User;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private Long id;

    private String username;

    private String displayName;

    private String email;

    private Integer status;

    private String country;

    private String bio;

    private String photoUrl;

    private Integer totalMatches;

    private Integer wins;

    private Integer draws;

    private Integer losses;

    private Integer currentTrophies;

    private Integer worldRank;

    private Integer maxRating;

    private Date createdAt;
}
