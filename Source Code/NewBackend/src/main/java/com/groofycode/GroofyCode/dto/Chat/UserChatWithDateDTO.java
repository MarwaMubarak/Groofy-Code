package com.groofycode.GroofyCode.dto.Chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class UserChatWithDateDTO {
    private UsersChatDTO chat;
    private Date lastMessageDate;

    public UserChatWithDateDTO() {
        chat = new UsersChatDTO();
    }
}
