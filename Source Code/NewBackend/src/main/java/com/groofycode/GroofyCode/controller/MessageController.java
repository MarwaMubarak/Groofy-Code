package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.MessageDTO;
import com.groofycode.GroofyCode.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @PutMapping("")
    ResponseEntity<Object> sendMessage(@RequestBody MessageDTO messageDTO) throws Exception {
        return messageService.sendMessage(messageDTO.getChatId(),messageDTO.getContent());
    }
}
