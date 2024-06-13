package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
