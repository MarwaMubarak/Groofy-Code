package com.groofycode.GroofyCode.controller.Chat;

import com.groofycode.GroofyCode.service.Chat.ChatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping()
    ResponseEntity<Object> create(@RequestBody String name) throws Exception {
        return chatService.create(name);
    }

    @GetMapping("/{clanId}")
    ResponseEntity<Object> getClanChatById(@PathVariable Long clanId) throws Exception {
        return chatService.getClanChatById(clanId);
    }


    @PutMapping("/{chatId}/addUser")
    ResponseEntity<Object> addUser(@PathVariable Long chatId, @RequestBody Long userId) throws Exception {
        return chatService.addUser(chatId, userId);
    }

    @PostMapping("/between")
    ResponseEntity<Object> getOrCreateChat(@RequestBody Long userId2) {
        return chatService.getOrCreateChat(userId2);
    }
}
