package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.model.ClanModel;
import com.groofycode.GroofyCode.model.Message;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.ClanRepository;
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


    public void sendMessageToClan(Message message, Long clanId) {
        // Set the clan for the message
        ClanModel clan = clanRepository.findById(clanId).orElseThrow(() -> new RuntimeException("Clan not found"));
        message.setClan(clan);

        // Set the user for the message (You need to get the authenticated user here)
        UserModel user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
        message.setUser(user);

        // Save the message to the database
        messageRepository.save(message);
    }
}
