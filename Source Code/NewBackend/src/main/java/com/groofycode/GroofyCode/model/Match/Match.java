package com.groofycode.GroofyCode.model.Match;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "matches")
@Entity
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID matchId;

    private Integer duration;

    // @TODO -> Problem model relation

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserMatch> users;

    @CreationTimestamp
    private Date createdAt;

    public Match() {
        this.matchId = UUID.randomUUID();
        this.users = new ArrayList<>();
    }

    public void AddToMatch(UserMatch userMatch) {
        if (!this.users.contains(userMatch)) {
            this.users.add(userMatch);
        }
    }
}
