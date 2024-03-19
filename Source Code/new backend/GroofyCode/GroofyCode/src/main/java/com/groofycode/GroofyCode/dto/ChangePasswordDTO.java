package com.groofycode.GroofyCode.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    private String currentPassword;
    private String password;
    private String confirmPassword;

    // Getters and setters
}