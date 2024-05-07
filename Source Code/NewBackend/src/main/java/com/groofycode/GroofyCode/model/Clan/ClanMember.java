package com.groofycode.GroofyCode.model.Clan;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "clan_members")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ClanMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "clan_id")
    private ClanModel clan;

    public ClanMember(UserModel user, ClanModel clanModel ,String role) {
        this.user = user;
        this.clan = clanModel;
        this.role = role;
    }
}
