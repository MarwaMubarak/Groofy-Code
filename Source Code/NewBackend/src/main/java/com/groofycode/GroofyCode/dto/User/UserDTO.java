package com.groofycode.GroofyCode.dto.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String displayName;

    private String email;

    private Integer status;

    private String country;

    private String bio;

    private String photoUrl;

    private String accountColor;

    private String clanName;

    private Integer totalMatches;

    private String notifyCnt;

    private Integer wins;

    private Integer draws;

    private Integer losses;

    private Integer currentTrophies;

    private Integer worldRank;

    private Integer maxRating;

    private String friendshipStatus;

    private Date createdAt;
}
