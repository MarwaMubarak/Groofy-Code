package com.groofycode.GroofyCode.model.Chat;

import com.groofycode.GroofyCode.model.Clan.ClanModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

@Table(name = "chats")
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Message> messages;

    @CreationTimestamp
    private Date createdAt;

    public Chat() {
        this.messages = new ArrayList<>();
    }
}
