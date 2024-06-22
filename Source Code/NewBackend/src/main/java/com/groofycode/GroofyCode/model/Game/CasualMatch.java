package com.groofycode.GroofyCode.model.Game;


import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CASUAL")
public class CasualMatch extends Game {

    public CasualMatch() {
        setGameType(GameType.CASUAL);
    }

    public CasualMatch(UserModel player1, UserModel player2, LocalDateTime startTime) {
        super(player1, player2, startTime);
        setGameType(GameType.CASUAL);
    }

}
