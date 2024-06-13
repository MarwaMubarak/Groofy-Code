package com.groofycode.GroofyCode.controller.Clan;

import com.groofycode.GroofyCode.dto.MessageDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
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
    @MessageMapping("/clan/{clanName}/sendMessage")
    public void sendMessageToClan(@Payload MessageDTO message, @DestinationVariable String clanName) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        messagingTemplate.convertAndSend(String.format("/clanTCP/%s/chat", clanName), message);
    }
}