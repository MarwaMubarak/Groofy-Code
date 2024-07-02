package com.groofycode.GroofyCode.dto.Notification;

import com.groofycode.GroofyCode.model.Notification.NotificationType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class MatchInvitationNotificationDTO extends NotificationDTO {
    private Long team1ID;
    private Long team2ID;
    private Long invitationID;
    private boolean isAdmin;
}
