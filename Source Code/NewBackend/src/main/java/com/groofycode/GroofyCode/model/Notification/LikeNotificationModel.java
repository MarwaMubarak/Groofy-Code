package com.groofycode.GroofyCode.model.Notification;
import com.groofycode.GroofyCode.model.Post.PostModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LikeNotificationModel extends NotificationModel {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostModel post;

}
