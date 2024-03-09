package com.groofycode.GroofyCode.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Getter
@Setter
public class UserDTO {
    @NotBlank
    @Size(min = 1, max = 100)
    private String username;

    @NotBlank
    @Email
    @Size(min = 4, max = 256)
    private String email;

    @NotBlank
    @Size(min = 8, max = 256)
    private String password;

    @Size(max = 256)
    private String firstname;

    @Size(max = 256)
    private String lastname;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
    private String city;

    @Size(max = 1000)
    private String bio;

    private Long clanId;


    public UserDTO(String username, String email, String firstname, String lastname, String country, String city, String bio, Long clanId) {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.city = city;
        this.bio = bio;
        this.clanId = clanId;
    }
    public UserDTO( String username, String email, String password, String firstname, String lastname, String country, String city, String bio, Long clanId) {
        this.username = username;
        this.email = email;
        this.password=password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.city = city;
        this.bio = bio;
        this.clanId = clanId;
    }

    public UserDTO(){}

}
