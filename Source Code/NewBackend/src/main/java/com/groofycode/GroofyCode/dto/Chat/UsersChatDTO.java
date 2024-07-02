package com.groofycode.GroofyCode.dto.Chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersChatDTO {
    private Long id;

    private Long receiverId;

    private String receiverName;

    private String receiverPhoto;

    private String receiverColor;

    private Integer unreadCount;

    List<MessageDTO> messages;
}
