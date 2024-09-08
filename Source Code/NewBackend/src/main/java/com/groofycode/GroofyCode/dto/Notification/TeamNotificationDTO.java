package com.groofycode.GroofyCode.dto.Notification;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class TeamNotificationDTO extends NotificationDTO {
    private Long teamId;
    private String teamName;

}
