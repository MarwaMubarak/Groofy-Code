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

    @GetMapping
    public ResponseEntity<Object> getAllNotifications(@RequestParam(value = "p", required = false, defaultValue = "0") Integer page) throws Exception {
        return notificationService.getAllNotifications(page);
    }

    @GetMapping("/normal")
    public ResponseEntity<Object> GetNormalNotifications(@RequestParam(value = "p", required = false, defaultValue = "0") Integer page) throws Exception {
        return notificationService.getNormalNotifications(page);
    }

    @GetMapping("/friend")
    public ResponseEntity<Object> getUserFriendNotifications(@RequestParam(value = "p", required = false, defaultValue = "0") Integer page) throws Exception {
        return notificationService.getUserFriendNotifications(page);
    }

    @PutMapping("/markRead/{notificationId}")
    public ResponseEntity<Void> markRead(@PathVariable Long notificationId) {
        notificationService.markRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
