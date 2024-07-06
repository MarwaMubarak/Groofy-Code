package com.groofycode.GroofyCode.model.Game;

import lombok.Getter;

@Getter
public enum GameType {
    SOLO("Solo"),
    RANKED("Ranked"),
    CASUAL("Casual"),
    TEAM("Team"),
    BEAT_A_FRIEND("Friendly"),  // New game type
    VELOCITY("Velocity");

    private final String value;

    GameType(String value) {
        this.value = value;
    }

}