package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Notification.FriendNotificationModel;
import com.groofycode.GroofyCode.model.Notification.NotificationType;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendNotificationRepository extends JpaRepository<FriendNotificationModel, Long> {
    List<FriendNotificationModel> findByReceiver(UserModel receiver);
    FriendNotificationModel findBySenderAndReceiverAndNotificationType(String sender, UserModel receiver, NotificationType notificationType);

}
