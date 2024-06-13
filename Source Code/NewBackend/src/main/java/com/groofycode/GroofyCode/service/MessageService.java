package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.ChatDTO;
import com.groofycode.GroofyCode.dto.MessageDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Clan.ClanModel;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.ChatRepository;
import com.groofycode.GroofyCode.repository.Clan.ClanRepository;
import com.groofycode.GroofyCode.repository.MessageRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private ClanRepository clanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private  ModelMapper modelMapper;


    public ResponseEntity<Object> sendMessage(Long chatId, String content) throws Exception{
        try {

            //check if the chat exist
            Optional<Chat> chatModel = chatRepository.findById(chatId);
            if (chatModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //send message
            Message message = new Message();
            message.setUserId(userInfo.getUserId());
            message.setContent(content);
            message.setChatId(chatId);

            messageRepository.save(message);
            MessageDTO messageDTO = modelMapper.map(message,MessageDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Message Sent Successfully!", messageDTO));

        }catch (Exception e){
            throw new Exception(e);
        }
    }



//    public void sendMessageToClan(MessageDTO messageDTO, Long clanId) {
//        // Set the clan for the message
//        ClanModel clan = clanRepository.findById(clanId).orElseThrow(() -> new RuntimeException("Clan not found"));
//        Message message = new Message();
//
//        message.setClan(clan);
//
//        // Set the user for the message (You need to get the authenticated user here)
//        UserModel user = userRepository.findById(messageDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
//        message.setUser(user);
//
//        message.setContent(messageDTO.getContent());
//
//        // Save the message to the database
//        messageRepository.save(message);
//    }
}
