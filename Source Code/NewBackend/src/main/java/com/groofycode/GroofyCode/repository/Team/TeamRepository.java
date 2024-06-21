package com.groofycode.GroofyCode.repository.Team;

import com.groofycode.GroofyCode.model.Team.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamModel, Long> {

}
