package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMatchInvitation extends MatchInvitation {

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private TeamModel team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private TeamModel team2;

    public TeamMatchInvitation(TeamModel team1, TeamModel team2, UserModel sender, UserModel receiver, Date sentAt) {
        super(sender, receiver, sentAt);
        this.team1 = team1;
        this.team2 = team2;
    }
}