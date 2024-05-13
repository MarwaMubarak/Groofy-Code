package com.groofycode.GroofyCode.service;
import com.groofycode.GroofyCode.dto.NotificationDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.NotificationRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;

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

    public List<NotificationDTO> getUserNotifications() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
        List<NotificationModel> notifications = notificationRepository.findByReceiver(userModel);
        return notifications.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
    }

    private NotificationDTO mapEntityToDTO(NotificationModel notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setBody(notification.getBody());
        dto.setSender(notification.getSender());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        // Map other fields if needed
        return dto;
    }
}
