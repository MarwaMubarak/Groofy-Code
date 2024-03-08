package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements UserDetails {
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


    @ManyToOne
    @JoinColumn(name = "clan_id")
    private ClanModel clan;


    public UserModel(Long id) {
        super();
        this.id = id;
    }

    public UserModel(String username, String password, Collection<GrantedAuthority> grantedAuthorityList) {
        super();
        this.username = username;
        this.password = password;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority>authorityList=new ArrayList<>();
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


//    @OneToMany(mappedBy = "user")
//    private List<BadgeModel> badges;

//    @OneToMany(mappedBy = "user")
//    private List<BadgeModel> selectedBadges;

    // Other fields and mappings

}

// You can add additional entity classes for related tables like Badge, Comment, Match, Submission, etc.
