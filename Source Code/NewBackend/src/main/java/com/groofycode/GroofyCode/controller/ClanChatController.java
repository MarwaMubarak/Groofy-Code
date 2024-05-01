package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.MessageDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Message;
import com.groofycode.GroofyCode.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class ClanChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Handle messages sent by users to a specific clan chat
    @MessageMapping("/clan/{clanId}/sendMessage")
    public void sendMessageToClan(@Payload MessageDTO message, @DestinationVariable Long clanId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        messagingTemplate.convertAndSendToUser(userInfo.getUsername(), "/asd", message);
    }
}