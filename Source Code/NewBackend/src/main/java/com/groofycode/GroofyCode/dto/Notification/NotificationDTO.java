package com.groofycode.GroofyCode.dto.Notification;

import com.groofycode.GroofyCode.model.Notification.NotificationType;
import lombok.*;

import java.util.Date;

import java.util.Date;

@Setter
@Getter
@Data
@NoArgsConstructor
public class NotificationDTO {
    private long id;
    private String body;
    private String sender;
    private Long senderId;
    private String img;
    private String color;
    private NotificationType notificationType;
    private Date createdAt;
    private boolean isRead;
    private String notifyCnt;
}
