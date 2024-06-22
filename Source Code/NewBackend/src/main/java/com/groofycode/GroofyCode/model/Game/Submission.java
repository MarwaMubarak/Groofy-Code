package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    private String language;

    private String code;

    private LocalDateTime submissionTime;

    private Integer result;

    // Constructors, getters, and setters
    public Submission() {
    }

    public Submission(Game game, UserModel user, String code, LocalDateTime submissionTime, Integer result) {
        this.game = game;
        this.user = user;
        this.code = code;
        this.submissionTime = submissionTime;
        this.result = result;
    }
}
