package com.groofycode.GroofyCode.repository;
import com.groofycode.GroofyCode.model.Notification.NotificationModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
    List<NotificationModel> findByReceiver(UserModel user);
    @Query("SELECT n FROM NotificationModel n WHERE n.receiver.id=?1")
    List<NotificationModel> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM NotificationModel n WHERE n.receiver=?1 AND n.isRetrieved=false")
    Integer countUnRetrievedByReceiver(UserModel user);
}
