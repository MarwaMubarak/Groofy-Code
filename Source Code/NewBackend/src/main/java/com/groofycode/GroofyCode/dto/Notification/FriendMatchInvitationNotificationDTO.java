package com.groofycode.GroofyCode.dto.Notification;


import com.groofycode.GroofyCode.model.Notification.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FriendMatchInvitationNotificationDTO {

    private Long id;
    private String body;
    private String sender; // Assuming sender is UserModel with getUsername() method
    private NotificationType notificationType;
    private Date createdAt;
    private Long friendMatchInvitationID;

    // Add other fields as needed

}
