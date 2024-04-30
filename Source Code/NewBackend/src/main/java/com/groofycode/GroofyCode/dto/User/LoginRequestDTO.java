package com.groofycode.GroofyCode.dto.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {
    @NotBlank
    private String emailOrUsername;
    @NotBlank
    private String password;
}
