package com.groofycode.GroofyCode.dto.Team;

import lombok.Data;

@Data
public class TeamInvitationDTO {
    private Long invitationId;
    private String teamName;
    private String senderUsername;
    private String receiverUsername;
}
