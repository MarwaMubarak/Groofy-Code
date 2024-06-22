package com.groofycode.GroofyCode.model.Notification;

import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Post.PostModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter

public class MatchNotificationModel extends NotificationModel {

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Game game;
}