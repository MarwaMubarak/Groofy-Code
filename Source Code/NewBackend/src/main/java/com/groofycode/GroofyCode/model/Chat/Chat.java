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


    public  Chat(){
        userIds = new ArrayList<>();
    }

    public  void  adduser(Long userId){
        if(userIds.isEmpty())
            userIds = new ArrayList<>();
        userIds.add(userId);
    }
    public boolean checkUserExist(Long userId){
        return userIds.contains(userId);
    }
}
