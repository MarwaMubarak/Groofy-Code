package com.groofycode.GroofyCode.dto.Notification;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LikeNotificationDTO extends NotificationDTO {
    private String content;
    private Long postId;
}
