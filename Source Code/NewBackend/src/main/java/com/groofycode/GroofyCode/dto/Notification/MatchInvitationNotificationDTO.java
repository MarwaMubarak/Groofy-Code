package com.groofycode.GroofyCode.dto.Notification;

import com.groofycode.GroofyCode.model.Notification.NotificationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class MatchInvitationNotificationDTO extends NotificationDTO {
    private Long team1ID;
    private Long team2ID;
    private Long invitationID;
}
