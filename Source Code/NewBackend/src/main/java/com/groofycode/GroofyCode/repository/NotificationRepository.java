package com.groofycode.GroofyCode.repository;
import com.groofycode.GroofyCode.model.Notification.LikeNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.Post.PostModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.transaction.Transactional;
import org.apache.el.stream.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
    List<NotificationModel> findByReceiver(UserModel user);
}
