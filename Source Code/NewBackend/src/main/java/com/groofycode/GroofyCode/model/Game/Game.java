package com.groofycode.GroofyCode.model.Game;


import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "game_type", discriminatorType = DiscriminatorType.STRING)
@Setter
@Getter
public abstract class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // This signifies a Many-To-One relationship with the User model
    private UserModel player1;

    @ManyToOne
    private UserModel player2;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions;

    @Transient
    private GameType gameType;


    @Transient
    private GameStatus gameStatus;

    // Constructors, getters, and setters
    public Game() {

    }

    public Game(UserModel player1, UserModel player2, LocalDateTime startTime) {
        this.player1 = player1;
        this.player2 = player2;
        this.startTime = startTime;

        setGameStatus(GameStatus.ONGOING);
    }

    public Integer getGameType() {
        return gameType != null ? gameType.ordinal() : null;
    }

    public Integer getGameStatus() {
        return gameStatus != null ? gameStatus.ordinal() : null;
    }
}
