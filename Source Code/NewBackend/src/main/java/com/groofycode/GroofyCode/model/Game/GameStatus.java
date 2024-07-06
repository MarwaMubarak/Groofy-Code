package com.groofycode.GroofyCode.model.Game;

import lombok.Getter;

@Getter
public enum GameStatus {
    ONGOING("ONGOING"),
    FINISHED("FINISHED"),
    OPPONENT_LEFT("OPPONENT_LEFT"),;

    private final String value;

    GameStatus(String value) {
        this.value = value;
    }

}
