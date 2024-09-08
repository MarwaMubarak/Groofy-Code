package com.groofycode.GroofyCode.repository.Game;

import com.groofycode.GroofyCode.model.Game.GameHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {

    Page<GameHistory> findByUserIdOrderByGameDateDesc(Long userId, Pageable p);
}
