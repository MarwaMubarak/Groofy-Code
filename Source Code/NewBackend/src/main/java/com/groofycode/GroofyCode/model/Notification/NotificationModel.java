package com.groofycode.GroofyCode.model.Notification;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "notifications")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public class NotificationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String body;

    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel receiver;

    @CreationTimestamp
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 255)  // Ensure this matches the altered column length
    private NotificationType notificationType;

}
