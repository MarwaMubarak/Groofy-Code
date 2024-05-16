package com.groofycode.GroofyCode.repository;

import java.util.Optional;
import com.groofycode.GroofyCode.model.Notification.LikeNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.Post.PostModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeNotificationRepository extends JpaRepository<LikeNotificationModel, Long> {
    NotificationModel findByPostAndSender(PostModel post, String sender);
    @Modifying
    @Transactional
    @Query("DELETE FROM LikeNotificationModel n WHERE n.post.id = :postId")
    void deleteNotificationByPostId(@Param("postId") Long postId);
}