package com.groofycode.GroofyCode.repository.Team;

import com.groofycode.GroofyCode.model.Team.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamModel, Long> {
    Optional<TeamModel> findById(Long id);


}
