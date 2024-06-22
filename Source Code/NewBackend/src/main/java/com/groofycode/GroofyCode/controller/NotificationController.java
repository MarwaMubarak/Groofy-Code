package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.Notification.NotificationDTO;
import com.groofycode.GroofyCode.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

//    @PostMapping
////    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
////        System.out.println("dohaa");
//////        NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
//////        return ResponseEntity.ok(createdNotification);
////    }


    @GetMapping("/like")
    public ResponseEntity<List<NotificationDTO>> getUserLikeNotifications() {
        System.out.println("dohhaaa");
        List<NotificationDTO> userNotifications = notificationService.getUserLikes();
        return ResponseEntity.ok(userNotifications);
    }
    @GetMapping("/friend")
    public ResponseEntity<List<NotificationDTO>> getUserFriendNotifications() {
        List<NotificationDTO> friendNotifications = notificationService.getUserFriendNotifications();
        return ResponseEntity.ok(friendNotifications);
    }
    @PutMapping("/markRead/{notificationId}")
    public ResponseEntity<Void> markRead(@PathVariable Long notificationId) {
        notificationService.markRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
