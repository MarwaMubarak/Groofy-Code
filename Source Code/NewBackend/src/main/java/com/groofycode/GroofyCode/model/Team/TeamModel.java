package com.groofycode.GroofyCode.model.Team;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "teams")
@Entity
@AllArgsConstructor
@Getter
@Setter
public class TeamModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamMember> members;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamInvitation> teamInvitations;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserModel creator;


    public TeamModel() {
        this.members = new ArrayList<>();
        this.teamInvitations = new ArrayList<>();
    }
    public boolean isMember(UserModel user) {
        return members.stream().anyMatch(member -> member.getUser().equals(user));
    }

    public boolean addMember(TeamMember member) {
        if (isMember(member.getUser())) {
            return false;
        }
        members.add(member);
        return true;
    }

    public boolean removeMember(UserModel user) {
        return members.removeIf(member -> member.getUser().equals(user));
    }
}
