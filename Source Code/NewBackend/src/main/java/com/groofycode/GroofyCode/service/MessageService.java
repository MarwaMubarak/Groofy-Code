package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.MessageDTO;
import com.groofycode.GroofyCode.model.Clan.ClanModel;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Clan.ClanRepository;
import com.groofycode.GroofyCode.repository.MessageRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private ClanRepository clanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;


    public void sendMessageToClan(MessageDTO messageDTO, Long clanId) {
        // Set the clan for the message
        ClanModel clan = clanRepository.findById(clanId).orElseThrow(() -> new RuntimeException("Clan not found"));
        Message message = new Message();

        message.setClan(clan);

        // Set the user for the message (You need to get the authenticated user here)
        UserModel user = userRepository.findById(messageDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        message.setUser(user);

        message.setContent(messageDTO.getContent());

        // Save the message to the database
        messageRepository.save(message);
    }
}
