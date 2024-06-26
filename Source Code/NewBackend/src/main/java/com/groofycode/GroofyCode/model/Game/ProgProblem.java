package com.groofycode.GroofyCode.model.Game;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "problem") // Specify table name explicitly to avoid any naming conflicts or defaults
@Getter
@Setter
public class ProgProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contest_id", length = 255)
    private String contestId;

    @Column(name = "index_name", length = 255) // Escape reserved keyword "index"
    private String index;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "solved_count")
    private Integer solvedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;

    // Getters and Setters
}
