package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.Notification.*;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Notification.*;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.FriendNotificationRepository;
import com.groofycode.GroofyCode.repository.LikeNotificationRepository;
import com.groofycode.GroofyCode.repository.NotificationRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final LikeNotificationRepository likeNotificationRepository;
    private final UserRepository userRepository;
    private final FriendNotificationRepository friendNotificationRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public NotificationService(NotificationRepository notificationRepository, LikeNotificationRepository likeNotificationRepository,
                               UserRepository userRepository, FriendNotificationRepository friendNotificationRepository, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.likeNotificationRepository = likeNotificationRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

        this.friendNotificationRepository = friendNotificationRepository;
    }

    public ResponseEntity<Object> getAllNotifications(Integer page) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (page == null || page < 0) page = 0;
            Pageable pageRequest = PageRequest.of(page, 6);
            List<NotificationModel> notifications = notificationRepository.findByUserId(userInfo.getUserId(), pageRequest);
            List<NotificationDTO> notificationsDTOS = notifications.stream().map(notification -> {
                if (!notification.isRetrieved()) {
                    notification.setRetrieved(true);
                    notificationRepository.save(notification);
                }
                NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
                notificationDTO.setSender(notification.getSender().getUsername());
                notificationDTO.setImg(notification.getSender().getPhotoUrl());
                return notificationDTO;
            }).toList();
            return ResponseEntity.ok(ResponseUtils.successfulRes("Notifications retrieved successfully", notificationsDTOS));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<NotificationDTO> getUserLikes() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
        List<LikeNotificationModel> notifications = likeNotificationRepository.findByReceiver(userModel);
        return notifications.stream().map(this::mapEntityToLikeDTO).collect(Collectors.toList());
    }

    public List<NotificationDTO> getUserFriendNotifications() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
        List<FriendNotificationModel> notifications = friendNotificationRepository.findByReceiver(userModel);
        return notifications.stream().map(this::mapEntityToFriendDTO).collect(Collectors.toList());
    }

    public void markRead(Long notificationId) {
        Optional<NotificationModel> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isPresent()) {
            NotificationModel notification = optionalNotification.get();
            notification.setRead(true);
            notificationRepository.save(notification);
        } else {
            throw new RuntimeException("Notification not found with id: " + notificationId);
        }
    }

    public NotificationDTO mapEntityToLikeDTO(LikeNotificationModel notification) {
        LikeNotificationDTO dto = new LikeNotificationDTO();
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender().getUsername());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setContent(notification.getPost().getContent());
        dto.setRead(notification.isRead());
        dto.setId(notification.getId());
        // Map other fields if needed
        return dto;
    }

    public NotificationDTO mapEntityToFriendDTO(FriendNotificationModel notification) {
        FriendNotificationDTO dto = new FriendNotificationDTO();
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender().getUsername());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setRead(notification.isRead());
        dto.setId(notification.getId());
        return dto;
    }

    public NotificationDTO mapEntityToTeamDTO(TeamNotificationModel notification) {
        TeamNotificationDTO dto = new TeamNotificationDTO();
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender().getUsername());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setRead(notification.isRead());
        dto.setId(notification.getId());
        dto.setTeamId(notification.getTeam().getId());
        dto.setTeamName(notification.getTeam().getName());
        return dto;
    }

    public MatchInvitationNotificationDTO mapEntityToDTO(MatchInvitationNotificationModel notification) {
        MatchInvitationNotificationDTO dto = new MatchInvitationNotificationDTO();
        dto.setId(notification.getId());
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender().getUsername());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setGameId(notification.getGame().getId());
        return dto;
    }
}
