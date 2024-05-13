package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.NotificationDTO;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/user")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications() {
        System.out.println("dohhaaa");
        List<NotificationDTO> userNotifications = notificationService.getUserNotifications();
        return ResponseEntity.ok(userNotifications);
    }
}
