package com.groofycode.GroofyCode.dto.User;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LoggedUserDTO {
    private Long id;
    private String username;
    private String displayName;
    private String email;
    private String firstname;
    private String lastname;
    private String country;
    private String[] badges;
    private String[] selectedBadges;
    private String[] friends;
    private boolean status;
    private boolean isOnline;
    private int totalMatch;
    private int highestTrophies;
    private int wins;
    private int loses;
    private int draws;
    private String division;
    private int Trophies;
    private String token;
    private String photoUrl;
    private Date createdAt;
    private Date updatedAt;
}
