package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.model.UserModel;

import java.util.List;

public class ClanDTO {
    private Long id;
    private String name;
    private UserModel leader;
    private List<UserModel> members;
    private String rank;

    // Constructors
    public ClanDTO() {}

    public ClanDTO(Long id, String name, UserModel leader, List<UserModel> members, String rank) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.rank = rank;
    }

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
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


}
