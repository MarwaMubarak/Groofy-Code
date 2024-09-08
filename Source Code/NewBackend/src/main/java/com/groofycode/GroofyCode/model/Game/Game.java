package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "game_type", discriminatorType = DiscriminatorType.STRING)
@Setter
@Getter
@AllArgsConstructor
public abstract class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany // Many-To-Many relationship to handle multiple players on both sides
    @JoinTable(
            name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserModel> players1;

    @ManyToMany
    @JoinTable(
            name = "game_opponents",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserModel> players2;

    private String problemUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double duration;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions;

    @Transient
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    // Constructors, getters, and setters
    public Game() {

    }

//    public Integer getGameType() {
//        return gameType != null ? gameType.ordinal() : null;
//    }

//    public Integer getGameStatus() {
//        return gameStatus != null ? gameStatus.ordinal() : null;
//    }
}
