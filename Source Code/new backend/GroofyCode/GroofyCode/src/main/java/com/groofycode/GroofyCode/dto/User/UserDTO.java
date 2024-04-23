package com.groofycode.GroofyCode.dto.User;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private Long id;

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String country;

    private String bio;

    private Long clanId;

    private String photoUrl;

    private Date createdAt;

    private Date updatedAt;
}
