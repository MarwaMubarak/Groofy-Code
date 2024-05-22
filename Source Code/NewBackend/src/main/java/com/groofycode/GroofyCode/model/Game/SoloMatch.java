package com.groofycode.GroofyCode.model.Game;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("SOLO")
public class SoloMatch extends Game {

    public SoloMatch() {
        setGameType(GameType.SOLO);
    }

    public SoloMatch(String player1, LocalDateTime startTime) {
        super(player1, null, startTime);
        setGameType(GameType.SOLO);

    }


}