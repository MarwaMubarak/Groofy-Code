package com.groofycode.GroofyCode.dto.Chat;

import com.groofycode.GroofyCode.dto.MessageDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
public class ChatDTO {

    private Long id;

    private String name;

    private Date createdAt;

    private List<Long> userIds;

    private List<MessageDTO> messages;


}