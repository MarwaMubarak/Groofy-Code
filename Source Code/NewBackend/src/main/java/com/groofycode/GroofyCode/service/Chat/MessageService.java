package com.groofycode.GroofyCode.service.Chat;

import com.groofycode.GroofyCode.dto.Chat.MessageDTO;
import com.groofycode.GroofyCode.dto.Chat.UserChatWithDateDTO;
import com.groofycode.GroofyCode.dto.Chat.UsersChatDTO;
import com.groofycode.GroofyCode.dto.Chat.UsersChatWrapperDTO;
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

import java.util.*;

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
            messageDTO.setChatId(chat.getId());
            messageDTO.setMessageType("USER");

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
            messageDTO.setCreatedAt(message.getCreatedAt());
            messageDTO.setChatId(clanChat.getId());
            messageDTO.setMessageType("CLAN");
            return messageDTO;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getMessagesChat(Long userId, Integer page) throws Exception {
        try {
            PageRequest pageRequest = PageRequest.of(page, 20);

            UserModel userModel = userRepository.findById(userId).orElse(null);
            if (userModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("User not found", null));
            }

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ChatUsers chatUsers = chatUsersRepository.findByFirstUserIdAndSecondUserId(userInfo.getUserId(), userId);
            if (chatUsers == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Chat not found", null));
            }

            Chat chat = chatUsers.getChat();

            List<Message> chatMessages = new ArrayList<>(messageRepository.findByChatIdOrderByCreatedAtDesc(chat.getId(), pageRequest).getContent());

            Collections.reverse(chatMessages);

            Integer unreadCount = messageRepository.countByChatIdAndUnread(userInfo.getUserId(), chat.getId());

            List<MessageDTO> chatMessagesDTO = chatMessages.stream().map(message -> {
                MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
                if (!message.getIsRead()) {
                    message.setIsRead(true);
                    messageRepository.save(message);
                }
                messageDTO.setUserId(message.getUserModel().getId());
                messageDTO.setUsername(message.getUserModel().getUsername());
                messageDTO.setPhotoUrl(message.getUserModel().getPhotoUrl());
                messageDTO.setAccountColor(message.getUserModel().getAccountColor());
                messageDTO.setCreatedAt(message.getCreatedAt());
                messageDTO.setChatId(chat.getId());
                return messageDTO;
            }).toList();

            UsersChatDTO usersChatDTO = new UsersChatDTO(chat.getId(), userModel.getId(), userModel.getUsername(),
                    userModel.getPhotoUrl(), userModel.getAccountColor(), unreadCount, chatMessagesDTO);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Chat retrieved Successfully!", usersChatDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Integer getCountUnreadChats() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<ChatUsers> chatUsers = chatUsersRepository.findAllChatsByUserId(userInfo.getUserId());
            Set<Integer> countUnreadChats = new HashSet<>();

            for(ChatUsers chatUser: chatUsers){
                if (chatUser.getFirstUser().getId().equals(userInfo.getUserId())) {
                    Integer unreadCount = messageRepository.countByChatIdAndUnread(chatUser.getFirstUser().getId(), chatUser.getChat().getId());
                    if(unreadCount > 0){
                        countUnreadChats.add(chatUser.getChat().getId().intValue());
                    }
                } else {
                    Integer unreadCount = messageRepository.countByChatIdAndUnread(chatUser.getSecondUser().getId(), chatUser.getChat().getId());
                    if(unreadCount > 0){
                        countUnreadChats.add(chatUser.getChat().getId().intValue());
                    }
                }
            }
            return countUnreadChats.size();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getUserChats(Integer page) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            PageRequest pageRequest = PageRequest.of(page, 12);
            List<ChatUsers> chatUsers = chatUsersRepository.findChatsByUserId(userInfo.getUserId(), pageRequest).getContent();
            Integer countUnreadChats = getCountUnreadChats();

            List<UserChatWithDateDTO> usersChatDTOS = new ArrayList<>();
            for(ChatUsers chatUser: chatUsers){
                UserChatWithDateDTO usersChatDTO = new UserChatWithDateDTO();
                if (chatUser.getFirstUser().getId().equals(userInfo.getUserId())) {
                    usersChatDTO.getChat().setId(chatUser.getChat().getId());
                    usersChatDTO.getChat().setReceiverName(chatUser.getSecondUser().getUsername());
                    usersChatDTO.getChat().setReceiverPhoto(chatUser.getSecondUser().getPhotoUrl());
                    usersChatDTO.getChat().setReceiverColor(chatUser.getSecondUser().getAccountColor());
                    usersChatDTO.getChat().setReceiverId(chatUser.getSecondUser().getId());
                    usersChatDTO.getChat().setUnreadCount(messageRepository.countByChatIdAndUnread(chatUser.getFirstUser().getId(), chatUser.getChat().getId()));
                } else {
                    usersChatDTO.getChat().setId(chatUser.getChat().getId());
                    usersChatDTO.getChat().setReceiverName(chatUser.getFirstUser().getUsername());
                    usersChatDTO.getChat().setReceiverPhoto(chatUser.getFirstUser().getPhotoUrl());
                    usersChatDTO.getChat().setReceiverColor(chatUser.getFirstUser().getAccountColor());
                    usersChatDTO.getChat().setReceiverId(chatUser.getFirstUser().getId());
                    usersChatDTO.getChat().setUnreadCount(messageRepository.countByChatIdAndUnread(chatUser.getSecondUser().getId(), chatUser.getChat().getId()));
                }
                usersChatDTO.setLastMessageDate(chatUser.getChat().getMessages().get(chatUser.getChat().getMessages().size() - 1).getCreatedAt());
                usersChatDTOS.add(usersChatDTO);
            }

            usersChatDTOS.sort(Comparator.comparing(UserChatWithDateDTO::getLastMessageDate).reversed());

            UsersChatWrapperDTO usersChatWrapperDTO = new UsersChatWrapperDTO(usersChatDTOS, countUnreadChats);

            return ResponseEntity.ok(ResponseUtils.successfulRes("Chats retrieved successfully", usersChatWrapperDTO));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
