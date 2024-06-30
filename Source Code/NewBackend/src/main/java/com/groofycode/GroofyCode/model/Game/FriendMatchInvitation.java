package com.groofycode.GroofyCode.model.Game;


import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class FriendMatchInvitation extends MatchInvitation {


    public FriendMatchInvitation(UserModel sender, UserModel receiver, Date sentAt) {
        super(sender, receiver, sentAt);

    }
}
