package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("RANKED")
public class RankedMatch extends Game {

    public RankedMatch() {
        setGameType(GameType.RANKED);
    }


    public RankedMatch(List<UserModel> team1, List<UserModel> team2, String problemUrl, LocalDateTime startTime, LocalDateTime endTime, double duration) {
        setPlayers1(team1);
        setPlayers2(team2);
        setProblemUrl(problemUrl);
        setStartTime(startTime);
        setEndTime(endTime);
        setDuration(duration);
        setGameStatus(GameStatus.ONGOING);
        setGameType(GameType.RANKED);
    }
}