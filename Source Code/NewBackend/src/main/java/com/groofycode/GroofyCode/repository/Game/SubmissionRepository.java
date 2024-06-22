package com.groofycode.GroofyCode.repository.Game;

import com.groofycode.GroofyCode.model.Game.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}