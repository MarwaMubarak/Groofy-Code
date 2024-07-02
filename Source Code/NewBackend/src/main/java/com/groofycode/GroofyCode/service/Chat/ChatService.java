package com.groofycode.GroofyCode.service.Chat;

import com.groofycode.GroofyCode.dto.Chat.*;
import com.groofycode.GroofyCode.dto.Chat.MessageDTO;
import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.Clan.ClanModel;
import com.groofycode.GroofyCode.repository.Chat.ChatRepository;
import com.groofycode.GroofyCode.repository.Clan.ClanRepository;
import com.groofycode.GroofyCode.repository.MessageRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final ClanRepository clanRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, ClanRepository clanRepository, ModelMapper modelMapper) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
        this.clanRepository = clanRepository;
    }

    public ResponseEntity<Object> getClanChatById(Long id, Integer page) throws Exception {
        try {
            ClanModel clanModel = clanRepository.fetchByClanId(id);

            if(clanModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan not found", null));
            }

            Chat chat = clanModel.getChat();

            ChatDTO chatDTO = modelMapper.map(chat, ClanChatDTO.class);

            PageRequest pageRequest = PageRequest.of(page, 10);

            List<Message> chatMessages = new ArrayList<>(messageRepository.findByChatIdOrderByCreatedAtDesc(chat.getId(), pageRequest).getContent());

            Collections.reverse(chatMessages);

            List<MessageDTO> messagesDTO = chatMessages.stream().map(message -> {
                MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
                messageDTO.setUsername(message.getUserModel().getUsername());
                messageDTO.setPhotoUrl(message.getUserModel().getPhotoUrl());
                messageDTO.setAccountColor(message.getUserModel().getAccountColor());
                messageDTO.setChatId(message.getChat().getId());
                messageDTO.setUserId(message.getUserModel().getId());
                messageDTO.setCreatedAt(message.getCreatedAt());
                return messageDTO;
            }).toList();

            chatDTO.setMessages(messagesDTO);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat retrieved successfully", chatDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
