package com.groofycode.GroofyCode.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String username;
    private String role;
}
