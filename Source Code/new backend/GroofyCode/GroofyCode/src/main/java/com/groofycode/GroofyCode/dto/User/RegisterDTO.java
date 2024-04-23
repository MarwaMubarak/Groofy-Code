package com.groofycode.GroofyCode.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 100, message = "Username must be between 4 and 100 characters")
    private String username;

    @NotBlank(message = "Display name is required")
    @Size(min = 4, max = 100, message = "Display name must be between 4 and 100 characters")
    private String displayName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(min = 4, max = 256, message = "Email must be between 4 and 256 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",
            message = "Password must be at least 8 characters and contains: 1 uppercase, 1 lowercase, 1 special Character, 1 digit")
    private String password;

    @NotBlank(message = "Country is required")
    @Size(min = 4, max = 100, message = "Country must be between 4 and 100 characters")
    private String country;
}
