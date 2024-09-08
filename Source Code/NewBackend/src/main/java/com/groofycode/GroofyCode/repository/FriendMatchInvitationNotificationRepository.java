package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Game.FriendMatchInvitation;
import com.groofycode.GroofyCode.model.Notification.FriendMatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendMatchInvitationNotificationRepository extends JpaRepository<FriendMatchInvitationNotificationModel, Long> {

    @Query("SELECT n FROM FriendMatchInvitationNotificationModel n WHERE " +
            "(n.sender = :sender AND n.receiver = :receiver) OR " +
            "(n.sender = :receiver AND n.receiver = :sender)")
    List<FriendMatchInvitationNotificationModel> findBySenderAndReceiverOrReceiverAndSender(
            @Param("sender") UserModel sender,
            @Param("receiver") UserModel receiver);

    List<FriendMatchInvitationNotificationModel> findByFriendMatchInvitation(FriendMatchInvitation friendMatchInvitation);
}
