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
                notificationDTO.setColor(notification.getSender().getAccountColor());
                notificationDTO.setSenderId(notification.getSender().getId());
                return notificationDTO;
            }).toList();
            return ResponseEntity.ok(ResponseUtils.successfulRes("Notifications retrieved successfully", notificationsDTOS));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getNormalNotifications(Integer page) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            PageRequest pageRequest = PageRequest.of(page, 7);
            List<NotificationModel> notifications = notificationRepository.findNormalNotificationsByUserId(userInfo.getUserId(), pageRequest).getContent();

            List<NotificationDTO> notificationDTOS = notifications.stream().map(notify -> {
                if (!notify.isRetrieved()) {
                    notify.setRetrieved(true);
                    notificationRepository.save(notify);
                }
                NotificationDTO notificationDTO = modelMapper.map(notify, NotificationDTO.class);
                notificationDTO.setBody(notify.getBody());
                notificationDTO.setSender(notify.getSender().getUsername());
                notificationDTO.setImg(notify.getSender().getPhotoUrl());
                notificationDTO.setColor(notify.getSender().getAccountColor());
                notificationDTO.setNotificationType(notify.getNotificationType());
                notificationDTO.setCreatedAt(notify.getCreatedAt());
                notificationDTO.setRead(notify.isRead());
                notificationDTO.setSenderId(notify.getSender().getId());
                if (notify instanceof FriendMatchInvitationNotificationModel) {
                    notificationDTO.setInvitationId(((FriendMatchInvitationNotificationModel) notify).getFriendMatchInvitation().getId());
                }
                return notificationDTO;
            }).toList();

            return ResponseEntity.ok(ResponseUtils.successfulRes("Notifications retrieved successfully", notificationDTOS));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getUserFriendNotifications(Integer page) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            PageRequest pageRequest = PageRequest.of(page, 7);
            List<FriendNotificationModel> notifications = friendNotificationRepository.findByReceiver(userModel, pageRequest).getContent();
            return ResponseEntity.ok(ResponseUtils.successfulRes("Notifications retrieved successfully",
                    notifications.stream().map(notify -> {
                        if (!notify.isRetrieved()) {
                            notify.setRetrieved(true);
                            notificationRepository.save(notify);
                        }
                        return mapEntityToFriendDTO(notify);
                    }).toList()));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
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
        dto.setImg(notification.getSender().getPhotoUrl());
        dto.setColor(notification.getSender().getAccountColor());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setRead(notification.isRead());
        dto.setId(notification.getId());
        dto.setSenderId(notification.getSender().getId());
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
        dto.setInvitationId(notification.getInvitationId());
        return dto;
    }

    public MatchInvitationNotificationDTO mapEntityToDTO(MatchInvitationNotificationModel notification) {
        MatchInvitationNotificationDTO dto = new MatchInvitationNotificationDTO();
        dto.setId(notification.getId());
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender().getUsername());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setTeam1ID(notification.getTeam1().getId());
        dto.setTeam2ID(notification.getTeam2().getId());
        dto.setInvitationID(notification.getMatchInvitation().getId());
        return dto;
    }

    public FriendMatchInvitationNotificationDTO mapEntityToDTO(FriendMatchInvitationNotificationModel notification) {
        FriendMatchInvitationNotificationDTO dto = new FriendMatchInvitationNotificationDTO();
        dto.setId(notification.getId());
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender().getUsername());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setFriendMatchInvitationID(notification.getFriendMatchInvitation().getId());
        return dto;
    }
}
