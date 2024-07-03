package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.model.Game.TeamMatchInvitation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeamMatchInvitationDTO {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private boolean isAccepted;
    private Date sentAt;
    private Long team1Id;
    private Long team2Id;
    List<playerDisplayDTO> team1Players;
    List<playerDisplayDTO> team2Players;
    private boolean isSender;

    public TeamMatchInvitationDTO(TeamMatchInvitation teamMatchInvitation) {
        this.id = teamMatchInvitation.getId();
        this.senderId = teamMatchInvitation.getSender().getId();
        this.receiverId = teamMatchInvitation.getReceiver().getId();
        this.isAccepted = teamMatchInvitation.isAccepted();
        this.sentAt = teamMatchInvitation.getSentAt();
        this.team1Id = teamMatchInvitation.getTeam1().getId();
        this.team2Id = teamMatchInvitation.getTeam2().getId();
    }
}
