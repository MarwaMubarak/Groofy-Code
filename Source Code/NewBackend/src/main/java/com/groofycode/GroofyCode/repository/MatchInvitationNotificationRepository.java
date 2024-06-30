package com.groofycode.GroofyCode.repository;


import com.groofycode.GroofyCode.model.Notification.MatchInvitationNotificationModel;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchInvitationNotificationRepository extends JpaRepository<MatchInvitationNotificationModel, Long> {
    @Query("SELECT n FROM MatchInvitationNotificationModel n WHERE " +
            "(n.team1 = :team1 AND n.team2 = :team2) OR " +
            "(n.team1 = :team2 AND n.team2 = :team1)")
    List<MatchInvitationNotificationModel> findByTeams(@Param("team1") TeamModel team1, @Param("team2") TeamModel team2);

}
