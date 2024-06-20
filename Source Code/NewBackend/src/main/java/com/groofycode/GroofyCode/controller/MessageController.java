package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.MessageDTO;
import com.groofycode.GroofyCode.service.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageService messageService;

    @PutMapping("/send")
    ResponseEntity<Object> sendMessage(@RequestParam Long chatId, @RequestParam String content) throws Exception {
        return messageService.sendMessage(chatId,content);
    }

    @GetMapping("/{chatId}")
    ResponseEntity<Object> getMessagesChat(@PathVariable Long chatId) throws Exception {
        return messageService.getMessagesChat(chatId);
    }

}
