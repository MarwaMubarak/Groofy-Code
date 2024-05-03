package com.groofycode.GroofyCode.repository.Match;

import com.groofycode.GroofyCode.model.Match.UserMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {
    @Query("SELECT um FROM UserMatch um JOIN FETCH um.match WHERE um.userModel.username =:username AND um.state = 0")
    UserMatch fetchUserMatchByUsername(@Param("username") String username);

    @Query("SELECT um FROM UserMatch um JOIN FETCH um.match WHERE um.userModel.username =:username ORDER BY um.match.createdAt DESC")
    Page<UserMatch> findMatchesByUsername(@Param("username") String username, Pageable pageable);
}
