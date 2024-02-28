package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Email
    @Size(min = 4, max = 256)
    @Column(unique = true)
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

//    @OneToMany(mappedBy = "user")
//    private List<BadgeModel> badges;

//    @OneToMany(mappedBy = "user")
//    private List<BadgeModel> selectedBadges;

    // Other fields and mappings

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

//    public List<BadgeModel> getBadges() {
//        return badges;
//    }
//
//    public void setBadges(List<BadgeModel> badges) {
//        this.badges = badges;
//    }
//
//    public List<BadgeModel> getSelectedBadges() {
//        return selectedBadges;
//    }
//
//    public void setSelectedBadges(List<BadgeModel> selectedBadges) {
//        this.selectedBadges = selectedBadges;
//    }
}

// You can add additional entity classes for related tables like Badge, Comment, Match, Submission, etc.
