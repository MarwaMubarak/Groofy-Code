package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("SOLO")
public class SoloMatch extends Game {
    public SoloMatch() {
        setGameType(GameType.SOLO);
    }


    public SoloMatch(List<UserModel> players1, String problemUrl, LocalDateTime startTime, LocalDateTime endTime, double duration) {
        super();
        setPlayers1(players1);
        setProblemUrl(problemUrl);
        setStartTime(startTime);
        setEndTime(endTime);
        setDuration(duration);
        setGameStatus(GameStatus.ONGOING);
        setGameType(GameType.SOLO);
    }
}