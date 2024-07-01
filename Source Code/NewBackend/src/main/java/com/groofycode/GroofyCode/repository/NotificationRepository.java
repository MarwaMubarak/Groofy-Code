package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
    List<NotificationModel> findByReceiver(UserModel user);

    @Query("SELECT n FROM NotificationModel n WHERE n.receiver.id=?1 Order By n.createdAt DESC")
    List<NotificationModel> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM NotificationModel n WHERE n.receiver=?1 AND n.isRetrieved=false AND n.notificationType != 'FRIEND_REQUEST' " +
            "AND n.notificationType != 'FRIEND_ACCEPT'")
    Integer countNormalUnRetrievedByReceiver(UserModel user);

    @Query("SELECT COUNT(n) FROM NotificationModel n WHERE n.receiver=?1 AND n.isRetrieved=false AND (n.notificationType = 'FRIEND_REQUEST' " +
            "OR n.notificationType = 'FRIEND_ACCEPT')")
    Integer countFriendUnRetrievedByReceiver(UserModel user);

    @Query("SELECT n FROM NotificationModel n WHERE n.receiver.id=:id AND n.notificationType != 'FRIEND_REQUEST' " +
            "AND n.notificationType != 'FRIEND_ACCEPT' Order By n.createdAt DESC")
    Page<NotificationModel> findNormalNotificationsByUserId(Long id, Pageable pageable);
}
