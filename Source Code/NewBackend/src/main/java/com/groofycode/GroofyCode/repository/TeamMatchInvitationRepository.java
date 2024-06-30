package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Game.TeamMatchInvitation;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamMatchInvitationRepository extends JpaRepository<TeamMatchInvitation, Long> {
    Optional<TeamMatchInvitation> findByTeam1AndTeam2(TeamModel team1, TeamModel team2);

    @Query("SELECT tmi FROM TeamMatchInvitation tmi WHERE " +
            "(tmi.team1 = :team1 AND tmi.team2 = :team2) OR " +
            "(tmi.team1 = :team2 AND tmi.team2 = :team1)")
    List<TeamMatchInvitation> findByTeams(@Param("team1") TeamModel team1, @Param("team2") TeamModel team2);

}
