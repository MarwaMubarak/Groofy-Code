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

    @Column
    private String name;

    @CreationTimestamp
    private Date createdAt;

    @Column
    private List<Long> userIds;


    @OneToOne(mappedBy = "chat")
    private ClanModel clan;

    @ManyToMany
    @JoinTable(
            name = "chat_participants",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserModel> participants = new HashSet<>();

    public Chat() {
        userIds = new ArrayList<>();
    }

    public void adduser(Long userId) {
        if (userIds.isEmpty())
            userIds = new ArrayList<>();
        userIds.add(userId);
    }

    public boolean checkUserExist(Long userId) {
        return userIds.contains(userId);
    }
}
