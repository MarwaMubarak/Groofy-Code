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
    @JoinColumn(name = "sender_id")
    private UserModel sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserModel receiver;

    private boolean isAccepted;

    private Date sentAt;

    public MatchInvitation( UserModel sender, UserModel receiver, Date sentAt) {
        this.sender = sender;
        this.receiver = receiver;
        this.sentAt = sentAt;
    }
}
