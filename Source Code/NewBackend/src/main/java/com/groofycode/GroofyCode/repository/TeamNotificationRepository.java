package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Notification.TeamNotificationModel;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamNotificationRepository extends JpaRepository<TeamNotificationModel, Long> {
    TeamNotificationModel findByReceiverAndTeam(UserModel receiver, TeamModel team);
}
