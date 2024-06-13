package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.ChatDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.ChatRepository;
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
import java.util.Vector;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> getById(Long id) throws Exception {
        try {
            Optional<Chat> chatOptional = chatRepository.findById(id);
            if (chatOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));
            }
            ChatDTO chatDTO = modelMapper.map(chatOptional.get(), ChatDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Badge retrieved successfully", chatDTO));
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

    public ResponseEntity<Object> delete(Long id) throws Exception {
        try {
            Optional<Chat> chatModel = chatRepository.findById(id);
            if (chatModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));
            chatRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat Deleted Successfully!", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> addUser(Long chatId, Long userId) throws Exception{
        try {


            //check that the user exist
            Optional<UserModel> userModel = userRepository.findById(userId);
            if(userModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found", null));

            //check if the chat exist
            Optional<Chat> chatModel = chatRepository.findById(chatId);
            if (chatModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));


            // check if he/she already in the chat
            if(chatModel.get().checkUserExist(userId))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("User Already In the Chat", null));

            //add him/her
            Chat chat = chatModel.get();
            chat.adduser(userId);
            chatRepository.save(chat);
            ChatDTO chatDTO = modelMapper.map(chat,ChatDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("User Added Successfully!", chatDTO));


        }catch (Exception e){
            throw new Exception(e);
        }
    }

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

            Chat chat = chatModel.get();
            chat.sendMessage(message);
            chatRepository.save(chat);
            ChatDTO chatDTO = modelMapper.map(chat,ChatDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Message Sent Successfully!", chatDTO));

        }catch (Exception e){
            throw new Exception(e);
        }
    }




}
