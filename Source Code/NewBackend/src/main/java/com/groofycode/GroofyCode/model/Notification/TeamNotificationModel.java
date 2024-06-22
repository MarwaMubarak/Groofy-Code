package com.groofycode.GroofyCode.model.Notification;

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
public class TeamNotificationModel extends NotificationModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private TeamModel team;

    private String teamName;
}
