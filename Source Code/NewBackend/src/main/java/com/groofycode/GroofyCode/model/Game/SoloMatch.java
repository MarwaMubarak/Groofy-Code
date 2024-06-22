package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("SOLO")
public class SoloMatch extends Game {
    public SoloMatch() {
        setGameType(GameType.SOLO);
    }

    public SoloMatch(UserModel player1, LocalDateTime startTime, String problemUrl) {
        super(player1, null, startTime, problemUrl);
        setGameType(GameType.SOLO);
    }
}