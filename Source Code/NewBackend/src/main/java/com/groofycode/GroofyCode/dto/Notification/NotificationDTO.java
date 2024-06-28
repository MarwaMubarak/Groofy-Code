package com.groofycode.GroofyCode.dto.Notification;

import com.groofycode.GroofyCode.model.Notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class NotificationDTO {
    private long id;
    private String body;
    private String sender;
    private String img;
    private String color;
    private NotificationType notificationType;
    private Date createdAt;
    private boolean isRead;
    private String notifyCnt;
}
