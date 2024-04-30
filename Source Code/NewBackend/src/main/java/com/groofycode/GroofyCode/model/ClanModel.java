package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "clans")
@Entity
@AllArgsConstructor
@Getter
@Setter
public class ClanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "leader", nullable = false)
    private UserModel leader;

    @OneToMany(mappedBy = "clan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages;


    @OneToMany(mappedBy = "clan", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<UserModel> members;

    @OneToMany
    @JoinColumn(nullable = false)
    private List<BadgeModel> badges;

    public ClanModel() {
        badges = new ArrayList<>();
    }

    public boolean addToClan(UserModel userModel) {
        if (members == null) {
            members = new ArrayList<>();
        }
        if (members.contains(userModel)) {
            return false;
        }
        members.add(userModel);
        return true;
    }

    public boolean removeFromClan(UserModel userModel) {
        if (!members.contains(userModel)) {
            return false;
        }
        members.remove(userModel);
        return true;
    }
}
