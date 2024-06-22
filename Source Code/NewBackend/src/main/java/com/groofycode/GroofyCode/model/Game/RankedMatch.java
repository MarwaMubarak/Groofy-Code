package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("RANKED")
public class RankedMatch extends Game {

    public RankedMatch() {
        setGameType(GameType.RANKED);
    }

    public RankedMatch(UserModel player1, UserModel player2, LocalDateTime startTime) {
        super(player1, player2, startTime);
        setGameType(GameType.RANKED);

    }


}