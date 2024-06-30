package com.groofycode.GroofyCode.service.Chat;

import com.groofycode.GroofyCode.dto.Chat.MessageDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Chat.ChatUsers;
import com.groofycode.GroofyCode.model.Chat.Message;
import com.groofycode.GroofyCode.model.Clan.ClanModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Chat.ChatRepository;
import com.groofycode.GroofyCode.repository.Chat.ChatUsersRepository;
import com.groofycode.GroofyCode.repository.Clan.ClanRepository;
import com.groofycode.GroofyCode.repository.MessageRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private ModelMapper modelMapper;

    @Autowired
    private ChatUsersRepository chatUsersRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendMessage(String username, String content) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            UserModel firstUser = userRepository.findById(userInfo.getUserId()).orElse(null);
            assert firstUser != null;

            UserModel secondUser = userRepository.findByUsername(username);

            ChatUsers chatUsers = chatUsersRepository.findByFirstUserIdAndSecondUserId(firstUser.getId(), secondUser.getId());
            Chat chat;


            if (chatUsers == null) {
                chat = new Chat();
                chat = chatRepository.save(chat);

                chatUsers = new ChatUsers(chat, firstUser, secondUser);
                chatUsersRepository.save(chatUsers);
            }

            chat = chatUsers.getChat();

            Message message = new Message();
            message.setUserModel(firstUser);
            message.setContent(content);
            message.setChat(chat);

            messageRepository.save(message);
            MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);

            messageDTO.setUserId(firstUser.getId());
            messageDTO.setUsername(firstUser.getUsername());
            messageDTO.setPhotoUrl(firstUser.getPhotoUrl());
            messageDTO.setAccountColor(firstUser.getAccountColor());
            messageDTO.setCreatedAt(message.getCreatedAt());

            messagingTemplate.convertAndSendToUser(firstUser.getUsername(), "/messages", ResponseUtils.successfulRes("New Message", messageDTO));
            messagingTemplate.convertAndSendToUser(secondUser.getUsername(), "/messages", ResponseUtils.successfulRes("New Message", messageDTO));

            ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Message Sent Successfully!", messageDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MessageDTO saveClanMessage(String clanName, MessageDTO messageDTO) throws Exception {
        try {
            ClanModel clan = clanRepository.fetchByNameIgnoreCase(clanName);
            if (clan == null) {
                throw new Exception("Clan not found");
            }

            Chat clanChat = clan.getChat();

            clanChat = entityManager.merge(clanChat);

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findById(userInfo.getUserId()).orElse(null);
            if (userModel == null) {
                throw new Exception("User not found");
            }

            Message message = new Message();
            message.setUserModel(userModel);
            message.setContent(messageDTO.getContent());
            message.setChat(clanChat);

            messageRepository.save(message);
            messageDTO = modelMapper.map(message, MessageDTO.class);
            messageDTO.setUserId(message.getUserModel().getId());
            messageDTO.setUsername(message.getUserModel().getUsername());
            messageDTO.setPhotoUrl(message.getUserModel().getPhotoUrl());
            messageDTO.setAccountColor(message.getUserModel().getAccountColor());
            return messageDTO;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getMessagesChat(Long chatId, Integer page) throws Exception {
        try {
            PageRequest pageRequest = PageRequest.of(page, 20);
            //check if the chat exist
            Optional<Chat> chatModel = chatRepository.findById(chatId);
            if (chatModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));
            }

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //if user part of the chat
//            if (!chatModel.get().checkUserExist(userInfo.getUserId())) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("User Not Allowed To Get The Chat!!", null));
//            }

            // get chat
            List<Message> chatMessages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId, pageRequest).getContent();

            List<MessageDTO> chatMessagesDTO = chatMessages.stream().map(message -> {
                MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
                messageDTO.setUserId(message.getUserModel().getId());
                messageDTO.setUsername(message.getUserModel().getUsername());
                messageDTO.setPhotoUrl(message.getUserModel().getPhotoUrl());
                messageDTO.setAccountColor(message.getUserModel().getAccountColor());
                return messageDTO;
            }).toList();

            if (chatMessagesDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Empty Chat!", chatMessagesDTO));
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat retrieved Successfully!", chatMessagesDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
