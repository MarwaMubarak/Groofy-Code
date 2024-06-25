package com.groofycode.GroofyCode.service.Game;

import com.groofycode.GroofyCode.model.Game.RankedMatch;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class MatchScheduler {

    @Autowired
    private GameService gameService;

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    @PostConstruct
    public void init() {
        taskScheduler.initialize();
    }

    public void scheduleRankCalculation(RankedMatch rankedMatch) {
        long delay = Duration.between(LocalDateTime.now(), rankedMatch.getEndTime()).toMillis();
        taskScheduler.schedule(() -> gameService.calculateRank(rankedMatch), new Date(System.currentTimeMillis() + delay));
    }
}
