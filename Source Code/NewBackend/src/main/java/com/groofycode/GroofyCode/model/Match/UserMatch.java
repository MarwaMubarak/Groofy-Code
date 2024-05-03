package com.groofycode.GroofyCode.model.Match;

import com.groofycode.GroofyCode.model.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Table(name = "user_matches")
@Entity
@Getter
@Setter
public class UserMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userMatchId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "match_id")
    private Match match;

    private Integer status;

    private Integer state;

    public UserMatch() {
        this.userMatchId = UUID.randomUUID();
        this.status = 0;
        this.state = 0;
    }
}
