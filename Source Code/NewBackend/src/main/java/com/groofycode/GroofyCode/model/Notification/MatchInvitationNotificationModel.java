package com.groofycode.GroofyCode.model.Notification;

import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Game.MatchInvitation;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MatchInvitationNotificationModel extends NotificationModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_invitation_id")
    private MatchInvitation matchInvitation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id")
    private TeamModel team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id")
    private TeamModel team2;

}

