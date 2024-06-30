package com.groofycode.GroofyCode.controller.Chat;

import com.groofycode.GroofyCode.service.Chat.MessageService;
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

    @GetMapping("/{chatId}")
    ResponseEntity<Object> getMessagesChat(@PathVariable Long chatId, @RequestParam(value = "p", required = false, defaultValue = "0") Integer page) throws Exception {
        return messageService.getMessagesChat(chatId, page);
    }
}
