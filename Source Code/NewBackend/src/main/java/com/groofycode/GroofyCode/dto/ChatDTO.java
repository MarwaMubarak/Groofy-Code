package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.model.Chat.Message;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Vector;
@Getter
@Setter
@Data
public class ChatDTO {

    private Long id;

    private String name;

    private Date createdAt;

    private List<Long> userIds;

    private List<Message> messages;

}