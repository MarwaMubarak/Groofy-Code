package com.groofycode.GroofyCode.security;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class LoginRequestDTO {
    @NotEmpty
    private String emailOrUsername;
    @NotEmpty
    private String password;

}
