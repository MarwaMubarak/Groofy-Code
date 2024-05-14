package com.groofycode.GroofyCode.repository;

import java.util.Optional;
import com.groofycode.GroofyCode.model.Notification.LikeNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.Post.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeNotificationRepository extends JpaRepository<LikeNotificationModel, Long> {
    NotificationModel findByPostAndSender(PostModel post, String sender);
}