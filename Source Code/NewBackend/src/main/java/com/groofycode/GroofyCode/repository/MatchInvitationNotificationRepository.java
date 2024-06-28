package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Notification.MatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  MatchInvitationNotificationRepository extends JpaRepository<MatchInvitationNotificationModel, Long> {
    MatchInvitationNotificationModel findByReceiverAndGame(UserModel receiver, Game game);
}
