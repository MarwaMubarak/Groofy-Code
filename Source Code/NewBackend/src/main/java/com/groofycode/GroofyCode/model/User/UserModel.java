package com.groofycode.GroofyCode.model.User;

import com.groofycode.GroofyCode.model.Badge.BadgeModel;
import com.groofycode.GroofyCode.model.Clan.ClanMember;
import com.groofycode.GroofyCode.model.Clan.ClanRequest;
import com.groofycode.GroofyCode.model.Match.UserMatch;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.Post.PostModel;
import jakarta.persistence.*;

import java.util.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String displayName;

    @Column(length = 100)
    private String country;

    @Column(length = 1000)
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String photoUrl;

    private String accountColor;

    private Integer status;

    private Integer totalMatches;

    private Integer wins;

    private Integer draws;

    private Integer losses;

    private Integer currentTrophies;

    private Integer worldRank;

    private Integer maxRating;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Message> messages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostModel> posts;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ClanMember clanMember;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ClanRequest clanRequest;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "badges_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "badge_id"))
    private List<BadgeModel> badges;

    @OneToMany(mappedBy = "userModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserMatch> userMatches;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationModel> notifications;



    @CreationTimestamp
    private Date createdAt;

    public UserModel() {
        this.accountColor = "#00aaff";
        this.currentTrophies = 0;
        this.worldRank = 0;
        this.maxRating = 0;
        this.totalMatches = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.status = 0;
        this.userMatches = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
}
