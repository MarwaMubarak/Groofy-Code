package com.groofycode.GroofyCode.dto.Chat;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private  Long id;
    private Long userId;
    private String content;
    private Date createdAt;
    private Long chatId;




}
