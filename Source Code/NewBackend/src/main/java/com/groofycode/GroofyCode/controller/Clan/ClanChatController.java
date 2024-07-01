package com.groofycode.GroofyCode.controller.Clan;

import com.groofycode.GroofyCode.dto.Chat.MessageDTO;
import com.groofycode.GroofyCode.service.Chat.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@SecurityRequirement(name = "bearerAuth")
@Controller
public class ClanChatController {
    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;

    @Autowired
    public ClanChatController(MessageService messageService, SimpMessagingTemplate simpMessagingTemplate) {
        this.messagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/clan/{clanName}/sendMessage")
    public void sendMessageToClan(@Payload MessageDTO message, @DestinationVariable String clanName) throws Exception {
        message = messageService.saveClanMessage(clanName, message);
        messagingTemplate.convertAndSend(String.format("/clanTCP/%s/chat", clanName), message);
    }
}