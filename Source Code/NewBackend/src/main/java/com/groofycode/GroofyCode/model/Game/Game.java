package com.groofycode.GroofyCode.model.Game;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "game_type", discriminatorType = DiscriminatorType.STRING)
@Setter
@Getter
public abstract class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player1;

    private String player2;

    private LocalDateTime startTime;

    @Transient
    private GameType gameType;


    // Constructors, getters, and setters
    public Game() {

    }

    public Game(String player1, String player2, LocalDateTime startTime) {
        this.player1 = player1;
        this.player2 = player2;
        this.startTime = startTime;
    }
    public Integer getGameType() {
        return gameType != null ? gameType.ordinal() : null;
    }

}
