package com.groofycode.GroofyCode.repository.Team;

import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.Team.TeamInvitation;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {
    Optional<TeamInvitation> findByTeamAndReceiver(TeamModel team, UserModel receiver);
    List<TeamInvitation> findByReceiver(UserModel receiver);

    @Modifying
    @Transactional
    void deleteAllByTeam(TeamModel team);

    List<TeamInvitation> findByTeam(TeamModel team);
    List<TeamInvitation> findAllByTeam(TeamModel team);

}
