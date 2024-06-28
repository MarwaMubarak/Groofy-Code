package com.groofycode.GroofyCode.service.Chat;

import com.groofycode.GroofyCode.dto.Chat.*;
import com.groofycode.GroofyCode.dto.Chat.MessageDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.ChatRepository;
import com.groofycode.GroofyCode.repository.MessageRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> getClanChatById(Long id) throws Exception {
        try {
            Optional<Chat> chatOptional = chatRepository.findById(id);

            if (chatOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));
            }

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //if user part of the chat
            if (!chatOptional.get().checkUserExist(userInfo.getUserId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("User Not Allowed To Get The Chat!!", null));

            ChatDTO chatDTO = modelMapper.map(chatOptional.get(), ClanChatDTO.class);

            List<Message> chatMessages = messageRepository.findByChatId(id);
            List<MessageDTO> messagesDTO = chatMessages.stream()
                    .map(message -> modelMapper.map(message, MessageDTO.class)).toList();
            chatDTO.setMessages(messagesDTO);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat retrieved successfully", chatDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> create(String name) throws Exception {
        try {

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Chat> chatByName = chatRepository.findByName(name);
            if (chatByName.isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.unsuccessfulRes("Chat with this name already exists.", null));
            Chat chat = new Chat();
            chat.setName(name);
            chat.adduser(userInfo.getUserId());
            chatRepository.save(chat);
            ChatDTO chatDTO = modelMapper.map(chat, ChatDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Chat Created Successfully!", chatDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> addUser(Long chatId, Long userId) throws Exception {
        try {


            //check that the user exist
            Optional<UserModel> userModel = userRepository.findById(userId);
            if (userModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found", null));

            //check if the chat exist
            Optional<Chat> chatModel = chatRepository.findById(chatId);
            if (chatModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));


            // check if he/she already in the chat
            if (chatModel.get().checkUserExist(userId))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("User Already In the Chat", null));

            //add him/her
            Chat chat = chatModel.get();
            chat.adduser(userId);
            chatRepository.save(chat);
            ChatDTO chatDTO = modelMapper.map(chat, ChatDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("User Added Successfully!", chatDTO));


        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public ResponseEntity<Object> getOrCreateChat(Long userId2) {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId1 = userInfo.getUserId();

            if (userId1.equals(userId2)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You can't create a chat with yourself", null));
            }

            Optional<Chat> existingChat = chatRepository.findChatByUsers(userId1, userId2);
            if (existingChat.isPresent()) {
                TwoUsersChatDTO chatDTO = modelMapper.map(existingChat.get(), TwoUsersChatDTO.class);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat found", chatDTO));
            }

            UserModel user1 = userRepository.findById(userId1).orElseThrow(() -> new RuntimeException("User not found"));
            UserModel user2 = userRepository.findById(userId2).orElseThrow(() -> new RuntimeException("User not found"));

            Chat newChat = new Chat();
            newChat.getParticipants().add(user1);
            newChat.getParticipants().add(user2);
            chatRepository.save(newChat);

            TwoUsersChatDTO chatDTO = modelMapper.map(newChat, TwoUsersChatDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Chat created successfully!", chatDTO));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("An error occurred", null));
        }
    }


}
