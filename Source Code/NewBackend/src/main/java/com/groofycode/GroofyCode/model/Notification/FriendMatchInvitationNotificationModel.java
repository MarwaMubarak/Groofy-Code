package com.groofycode.GroofyCode.model.Notification;

import com.groofycode.GroofyCode.model.Game.FriendMatchInvitation;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class FriendMatchInvitationNotificationModel extends NotificationModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_match_invitation_id")
    private FriendMatchInvitation friendMatchInvitation;

}
