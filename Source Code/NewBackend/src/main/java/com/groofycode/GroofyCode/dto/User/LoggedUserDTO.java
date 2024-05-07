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
    private String country;
    private String[] badges;
    private String[] selectedBadges;
    private String[] friends;
    private boolean status;
    private boolean isOnline;
    private boolean inClan;
    private boolean isLeader;
    private int Trophies;
    private String token;
    private String photoUrl;
    private Date createdAt;
}
