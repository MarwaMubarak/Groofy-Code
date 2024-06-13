package com.groofycode.GroofyCode.model.Chat;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Table(name = "chats")
@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @Column
    private List<Message> messages;

    public  void  adduser(Long userId){
        if(userIds.isEmpty())
            userIds = new ArrayList<>();
        userIds.add(userId);
    }
    public void sendMessage(Message message){
        if(messages.isEmpty())
            messages = new ArrayList<>();
        messages.add(message);
    }
    public boolean checkUserExist(Long userId){
        return userIds.contains(userId);
    }
}
