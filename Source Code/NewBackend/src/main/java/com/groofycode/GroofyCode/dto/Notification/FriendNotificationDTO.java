package com.groofycode.GroofyCode.dto.Notification;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
public class FriendNotificationDTO extends NotificationDTO {
    private String friendNotifyCnt;
}
