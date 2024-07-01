package com.groofycode.GroofyCode.repository.Team;

import com.groofycode.GroofyCode.model.Team.TeamModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;


public interface TeamRepository extends JpaRepository<TeamModel, Long> {
    Optional<TeamModel> findById(Long id);

    List<TeamModel> findByNameStartingWith(String prefix);

    Page<TeamModel> findByNameStartingWith(String prefix, Pageable pageable);

    Page<TeamModel> findByCreator(UserModel creator, Pageable pageable);

}
