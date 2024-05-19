package com.groofycode.GroofyCode.model.NewMatch;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // You can also use JOINED or TABLE_PER_CLASS
@DiscriminatorColumn(name = "match_type")
@Setter
@Getter
public abstract class MatchModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private UserModel user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private UserModel user2;

    private Instant matchTime;

    public abstract String getMatchType();

    public MatchModel() {}

    public MatchModel(UserModel user1, UserModel user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.matchTime = Instant.now();
    }

    public MatchModel(UserModel user1) {
        this.user1 = user1;
        this.matchTime = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchModel match = (MatchModel) o;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

