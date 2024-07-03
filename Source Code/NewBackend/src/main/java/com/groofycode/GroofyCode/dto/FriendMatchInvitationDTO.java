package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.model.Game.FriendMatchInvitation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FriendMatchInvitationDTO {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private boolean isAccepted;
    private Date sentAt;
    List<PlayerDisplayDTO> team1Players;
    List<PlayerDisplayDTO> team2Players;
    private boolean isSender;


    public FriendMatchInvitationDTO(FriendMatchInvitation friendMatchInvitation) {
        this.id = friendMatchInvitation.getId();
        this.senderId = friendMatchInvitation.getSender().getId();
        this.receiverId = friendMatchInvitation.getReceiver().getId();
        this.isAccepted = friendMatchInvitation.isAccepted();
        this.sentAt = friendMatchInvitation.getSentAt();
    }
}
