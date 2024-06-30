package com.groofycode.GroofyCode.repository.Game;
import com.groofycode.GroofyCode.model.Game.MatchInvitation;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchInvitationRepository extends JpaRepository<MatchInvitation, Long> {
//    Optional<MatchInvitation> findByTeam1AndTeam2(TeamModel team1, TeamModel team2);
}
