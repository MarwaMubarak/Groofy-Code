package com.groofycode.GroofyCode.controller.Chat;

import com.groofycode.GroofyCode.dto.Chat.MessageDTO;
import com.groofycode.GroofyCode.service.Chat.ChatService;
import com.groofycode.GroofyCode.service.Chat.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    private final MessageService messageService;

    @Autowired
    public ChatController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @GetMapping("/{clanId}")
    ResponseEntity<Object> getClanChatById(@PathVariable Long clanId, @RequestParam(value = "p", required = false, defaultValue = "0") Integer page) throws Exception {
        return chatService.getClanChatById(clanId, page);
    }

    @MessageMapping("/user/{username}/sendMessage")
    public void sendMessageToUser(@Payload MessageDTO message, @DestinationVariable String username) throws Exception {
        messageService.sendMessage(username, message.getContent());
    }
}
