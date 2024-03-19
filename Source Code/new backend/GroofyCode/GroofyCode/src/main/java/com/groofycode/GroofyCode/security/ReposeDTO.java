package com.groofycode.GroofyCode.security;

import com.groofycode.GroofyCode.model.UserModel;
import lombok.Data;

@Data
public class ReposeDTO {
    private String _id;
    private String username;
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

    public ReposeDTO(UserModel userModel, String token) {
        this._id = userModel.getId().toString();
        this.username = userModel.getUsername();
        this.email = userModel.getEmail();
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.country = userModel.getCountry();
        this.token = token;
    }
}
