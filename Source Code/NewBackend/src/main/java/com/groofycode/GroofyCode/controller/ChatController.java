package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.service.BadgeService;
import com.groofycode.GroofyCode.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping()
    ResponseEntity<Object>createChat(@RequestBody String name) throws Exception {
        return chatService.create(name);
    }

    @GetMapping("/{chatId}")
    ResponseEntity<Object>getById(@PathVariable Long chatId) throws Exception {
        return chatService.getById(chatId);
    }

    @DeleteMapping("/{chatId}")
    ResponseEntity<Object>delete(@PathVariable Long chatId) throws Exception {
        return chatService.delete(chatId);
    }


    @PutMapping("/{chatId}/addUser")
    ResponseEntity<Object>addUser(@PathVariable Long chatId, @RequestBody Long userId) throws Exception {
        return chatService.addUser(chatId, userId);
    }
}
