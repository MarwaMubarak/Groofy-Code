package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "CLAN",uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class ClanModel {

    public ClanModel() {}


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @OneToOne
    @JoinColumn(name = "leader", nullable = false)
    private UserModel leader;

    @OneToMany(mappedBy = "clan")
    private List<UserModel> members;

    @Column(nullable = false)
    private String current_rank="Metal";


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserModel getLeader() {
        return leader;
    }

    public void setLeader(UserModel leader) {
        this.leader = leader;
    }

    public List<UserModel> getMembers() {
        return members;
    }

    public void setMembers(List<UserModel> members) {
        this.members = members;
    }

    public String getRank() {
        return current_rank;
    }

    public void setRank(String rank) {
        this.current_rank = rank;
    }

}
