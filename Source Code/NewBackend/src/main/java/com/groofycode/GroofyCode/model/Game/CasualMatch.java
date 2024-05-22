package com.groofycode.GroofyCode.model.Game;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CASUAL")
public class CasualMatch extends Game {

    public CasualMatch() {
        setGameType(GameType.CASUAL);
    }

    public CasualMatch(String player1, String player2, LocalDateTime startTime) {
        super(player1, player2, startTime);
        setGameType(GameType.CASUAL);
    }

}
