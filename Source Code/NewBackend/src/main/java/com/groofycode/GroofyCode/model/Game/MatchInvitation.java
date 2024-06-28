package com.groofycode.GroofyCode.model.Game;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserModel sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserModel receiver;

    private Date sentAt;
    public MatchInvitation(Game game, UserModel sender, UserModel receiver, Date sentAt) {
        this.game = game;
        this.sender = sender;
        this.receiver = receiver;
        this.sentAt = sentAt;
    }
}
