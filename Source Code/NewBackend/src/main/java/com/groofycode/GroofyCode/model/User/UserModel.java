package com.groofycode.GroofyCode.model.User;

import com.groofycode.GroofyCode.model.Badge.BadgeModel;
import com.groofycode.GroofyCode.model.Chat.ChatUsers;
import com.groofycode.GroofyCode.model.Clan.ClanMember;
import com.groofycode.GroofyCode.model.Clan.ClanRequest;
import com.groofycode.GroofyCode.model.Game.GameHistory;
import com.groofycode.GroofyCode.model.Game.ProgProblem;
import com.groofycode.GroofyCode.model.Match.UserMatch;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.Post.PostModel;
import com.groofycode.GroofyCode.model.Team.TeamMember;
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

    private Integer user_rating;

    private Integer worldRank;

    private Integer user_max_rating;

    private Long existingGameId;

    private Long existingInvitationId;


    private Integer rate_800_cnt;
    private Integer rate_900_cnt;
    private Integer rate_1000_cnt;
    private Integer rate_1100_cnt;
    private Integer rate_1200_cnt;
    private Integer rate_1300_cnt;
    private Integer rate_1400_cnt;
    private Integer rate_1500_cnt;
    private Integer rate_1600_cnt;
    private Integer rate_1700_cnt;
    private Integer rate_1800_cnt;
    private Integer rate_1900_cnt;
    private Integer rate_2000_cnt;
    private Integer rate_2100_cnt;
    private Integer rate_2200_cnt;
    private Integer rate_2300_cnt;
    private Integer rate_2400_cnt;
    private Integer rate_2500_cnt;
    private Integer rate_2600_cnt;
    private Integer rate_2700_cnt;
    private Integer rate_2800_cnt;
    private Integer rate_2900_cnt;
    private Integer rate_3000_cnt;
    private Integer rate_3100_cnt;
    private Integer rate_3200_cnt;
    private Integer rate_3300_cnt;
    private Integer rate_3400_cnt;
    private Integer rate_3500_cnt;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ProgProblem> solvedProblems;



    // Define the relationship with TeamMember
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamMember> teamMembers;


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
        this.user_rating = 1200;
        this.worldRank = 0;
        this.user_max_rating = 1200;
        this.totalMatches = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.status = 0;
        this.rate_800_cnt = 0;
        this.rate_900_cnt = 0;
        this.rate_1000_cnt = 0;
        this.rate_1100_cnt = 0;
        this.rate_1200_cnt = 0;
        this.rate_1300_cnt = 0;
        this.rate_1400_cnt = 0;
        this.rate_1500_cnt = 0;
        this.rate_1600_cnt = 0;
        this.rate_1700_cnt = 0;
        this.rate_1800_cnt = 0;
        this.rate_1900_cnt = 0;
        this.rate_2000_cnt = 0;
        this.rate_2100_cnt = 0;
        this.rate_2200_cnt = 0;
        this.rate_2300_cnt = 0;
        this.rate_2400_cnt = 0;
        this.rate_2500_cnt = 0;
        this.rate_2600_cnt = 0;
        this.rate_2700_cnt = 0;
        this.rate_2800_cnt = 0;
        this.rate_2900_cnt = 0;
        this.rate_3000_cnt = 0;
        this.rate_3100_cnt = 0;
        this.rate_3200_cnt = 0;
        this.rate_3300_cnt = 0;
        this.rate_3400_cnt = 0;
        this.rate_3500_cnt = 0;
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
