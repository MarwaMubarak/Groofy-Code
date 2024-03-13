package com.groofycode.GroofyCode.model;
import java.util.ArrayList;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class LikeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private PostModel post;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "liked_at", nullable = false)
    private Date likedAt;

    public LikeModel() {
        this.likedAt = new Date();
    }
}
