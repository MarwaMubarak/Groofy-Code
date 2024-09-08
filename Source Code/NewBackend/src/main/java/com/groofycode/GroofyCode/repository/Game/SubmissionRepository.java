package com.groofycode.GroofyCode.repository.Game;

import com.groofycode.GroofyCode.model.Game.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    @Query("SELECT s FROM Submission s WHERE s.user.id = ?1 AND s.game.id = ?2 ORDER BY s.submissionTime DESC")
    List<Submission> findByUserId(Long userId, Long gameId);

    @Query("SELECT s FROM Submission s WHERE s.game.id = ?1 ORDER BY s.submissionTime DESC")
    List<Submission> findByGameId(Long gameId);
}