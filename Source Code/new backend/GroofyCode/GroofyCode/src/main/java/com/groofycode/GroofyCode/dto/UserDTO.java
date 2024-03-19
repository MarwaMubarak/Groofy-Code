package com.groofycode.GroofyCode.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
//import javax.validation.constraints.*;

@Getter
@Setter
public class UserDTO {

    //    @NotNull(message = "ID must not be null")
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 100, message = "Username must be between 1 and 100 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(min = 4, max = 256, message = "Email must be between 4 and 256 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 256, message = "Password must be between 8 and 256 characters")
    private String password;

    @Size(max = 256, message = "First name must be less than or equal to 256 characters")
    private String firstname;

    @Size(max = 256, message = "Last name must be less than or equal to 256 characters")
    private String lastname;

    @Size(max = 100, message = "Country must be less than or equal to 100 characters")
    private String country;

    @Size(max = 100, message = "City must be less than or equal to 100 characters")
    private String city;

    @Size(max = 1000, message = "Bio must be less than or equal to 1000 characters")
    private String bio;

    private Long clanId;

    private String photoUrl;

    private Date createdAt;

    private Date updatedAt;


    public UserDTO(String username, String email, String firstname, String lastname, String country, String city, String bio, Long clanId, String photoUrl) {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.city = city;
        this.bio = bio;
        this.clanId = clanId;
        this.photoUrl = photoUrl;

    }

    public UserDTO(String username, String email, String password, String firstname, String lastname, String country, String city, String bio, Long clanId, String photoUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.city = city;
        this.bio = bio;
        this.clanId = clanId;
        this.photoUrl = photoUrl;
    }

    public UserDTO() {
        this.photoUrl = "https://images.are.na/eyJidWNrZXQiOiJhcmVuYV9pbWFnZXMiLCJrZXkiOiI4MDQwOTc0L29yaWdpbmFsX2ZmNGYxZjQzZDdiNzJjYzMxZDJlYjViMDgyN2ZmMWFjLnBuZyIsImVkaXRzIjp7InJlc2l6ZSI6eyJ3aWR0aCI6MTIwMCwiaGVpZ2h0IjoxMjAwLCJmaXQiOiJpbnNpZGUiLCJ3aXRob3V0RW5sYXJnZW1lbnQiOnRydWV9LCJ3ZWJwIjp7InF1YWxpdHkiOjkwfSwianBlZyI6eyJxdWFsaXR5Ijo5MH0sInJvdGF0ZSI6bnVsbH19?bc=0";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}

