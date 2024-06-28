package com.groofycode.GroofyCode.service.Chat;

import com.groofycode.GroofyCode.dto.Chat.MessageDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.ChatRepository;
import com.groofycode.GroofyCode.repository.Clan.ClanRepository;
import com.groofycode.GroofyCode.repository.MessageRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ResponseEntity<Object> sendMessage(Long chatId, String content) throws Exception{
        try {

            //check if the chat exist
            Optional<Chat> chatModel = chatRepository.findById(chatId);
            if (chatModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //if user part of the chat
            if(!chatModel.get().checkUserExist(userInfo.getUserId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("User Not Allowed To Send Message!!", null));


            //send message
            Message message = new Message();
            message.setUserId(userInfo.getUserId());
            message.setContent(content);
            message.setChatId(chatId);

            messageRepository.save(message);
            MessageDTO messageDTO = modelMapper.map(message,MessageDTO.class);

            List<Long> users = chatModel.get().getUserIds();
            for(Long userId : users){
                if(!Objects.equals(userId, userInfo.getUserId())){
                    UserModel currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
                    messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/messages", ResponseUtils.successfulRes("New Message", messageDTO));
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Message Sent Successfully!", messageDTO));

        }catch (Exception e){
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getMessagesChat(Long chatId) throws Exception{
        try {

            //check if the chat exist
            Optional<Chat> chatModel = chatRepository.findById(chatId);
            if (chatModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //if user part of the chat
            if(!chatModel.get().checkUserExist(userInfo.getUserId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("User Not Allowed To Get The Chat!!", null));

            // get chat
            List<Message> chatMessages = messageRepository.findByChatId(chatId);

            //sort chat based on thr date
            chatMessages.sort(Comparator.comparing(Message::getCreatedAt));

            List<MessageDTO> chatMessagesDTO = chatMessages.stream()
                    .map(message -> modelMapper.map(message, MessageDTO.class)).toList();

            if (chatMessagesDTO.isEmpty())
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Empty Chat!", chatMessagesDTO));


            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat retrieved Successfully!", chatMessagesDTO));

        }catch (Exception e){
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getMessagesChatPage(Long chatId, int page,int size) throws Exception{
        try {

            //check if the chat exist
            Optional<Chat> chatModel = chatRepository.findById(chatId);
            if (chatModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //if user part of the chat
            if(!chatModel.get().checkUserExist(userInfo.getUserId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("User Not Allowed To Get The Chat!!", null));


            Pageable pageable = PageRequest.of(page, size);
            // get chat
            Page<Message> chatMessages = messageRepository.findByChatId(chatId,pageable);

            //sort chat based on thr date
            //chatMessages.sort(Comparator.comparing(Message::getCreatedAt));

            List<MessageDTO> chatMessagesDTO = chatMessages.stream()
                    .map(message -> modelMapper.map(message, MessageDTO.class)).toList();

            if (chatMessagesDTO.isEmpty())
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Empty Chat!", chatMessagesDTO));


            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat retrieved Successfully!", chatMessagesDTO));

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
