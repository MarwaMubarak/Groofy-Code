package com.groofycode.GroofyCode.model.Game;


import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "game_history") // Specify table name explicitly to avoid any naming conflicts or defaults
@Getter
@Setter
public class GameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_type", length = 255)
    private String gameType;

    @Column(name = "game_result", length = 255)
    private String gameResult;

    @Column(name = "game_date")
    private LocalDateTime gameDate;

    @Column(name = "rating_change")
    private Integer ratingChange;

    @Column(name = "new_rating")
    private Integer newRating;

    @Column(name = "user_id")
    private Long userId;


}
