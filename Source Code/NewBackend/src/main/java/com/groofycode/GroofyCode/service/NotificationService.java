package com.groofycode.GroofyCode.service;
import com.groofycode.GroofyCode.dto.Notification.FriendNotificationDTO;
import com.groofycode.GroofyCode.dto.Notification.LikeNotificationDTO;
import com.groofycode.GroofyCode.dto.Notification.NotificationDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Notification.FriendNotificationModel;
import com.groofycode.GroofyCode.model.Notification.LikeNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.FriendNotificationRepository;
import com.groofycode.GroofyCode.repository.LikeNotificationRepository;
import com.groofycode.GroofyCode.repository.NotificationRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final SimpMessagingTemplate messagingTemplate;

    private final FriendNotificationRepository friendNotificationRepository;


    @Autowired
    public NotificationService(NotificationRepository notificationRepository, LikeNotificationRepository likeNotificationRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, FriendNotificationRepository friendNotificationRepository) {
        this.notificationRepository = notificationRepository;
        this.likeNotificationRepository = likeNotificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;

        this.friendNotificationRepository = friendNotificationRepository;
    }

    public void createNotification(NotificationDTO notificationDTO,UserModel receiver) {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = userInfo.getUsername();
        NotificationModel notification = new NotificationModel();
        notification.setBody(notificationDTO.getBody());
        notification.setReceiver(receiver);
        notification.setNotificationType(notificationDTO.getNotificationType());
        notification.setCreatedAt(new Date());
        notification.setSender(sender);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/notification", notificationDTO.getBody());
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
        dto.setSender(notification.getSender());
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
        dto.setSender(notification.getSender());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setRead(notification.isRead());
        dto.setId(notification.getId());
        return dto;
    }
}
