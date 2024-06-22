package com.groofycode.GroofyCode.repository.Team;

import com.groofycode.GroofyCode.model.Team.TeamMember;
import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TeamMembersRepository extends JpaRepository<TeamMember, Long> {
    @Modifying
    @Transactional
    void deleteByTeamAndUser(TeamModel team, UserModel user);

    @Modifying
    @Transactional
    void deleteAllByTeam(TeamModel team);

    List<TeamMember> findByTeam(TeamModel team);
    Optional<TeamMember> findByUserAndTeam(UserModel user, TeamModel team);
    @Query("SELECT COUNT(tm) FROM TeamMember tm WHERE tm.team = :team")
    int countByTeam(TeamModel team);
}
