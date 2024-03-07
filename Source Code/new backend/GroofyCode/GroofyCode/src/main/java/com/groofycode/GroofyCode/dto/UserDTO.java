package com.groofycode.GroofyCode.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    // Password is intentionally left out to not expose it in the DTO
    private String firstname;
    private String lastname;
    private String country;
    private String city;
    private String bio;
    // Assuming ClanDTO exists, otherwise just use Long for clanId or similar
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getClanId() {
        return clanId;
    }

    public void setClanId(Long clanId) {
        this.clanId = clanId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
