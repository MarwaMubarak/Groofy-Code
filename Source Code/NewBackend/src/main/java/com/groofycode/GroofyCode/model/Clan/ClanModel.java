package com.groofycode.GroofyCode.model.Clan;

import com.groofycode.GroofyCode.model.Chat.Chat;
import com.groofycode.GroofyCode.model.Chat.Message;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "clans")
@Entity
@AllArgsConstructor
@Getter
@Setter
public class ClanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String leader;

    private Integer wins;

    private Integer losses;

    private Integer worldRank;

//    @OneToMany(mappedBy = "clan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Message> messages;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @OneToMany(mappedBy = "clan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClanMember> members;

    @OneToMany(mappedBy = "clan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClanRequest> clanRequests;

    public ClanModel() {
        this.members = new ArrayList<>();
//        this.messages = new ArrayList<>();
        this.clanRequests = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
        this.worldRank = 0;
    }

    public boolean IsMember(ClanMember clanMember) {
        return members.contains(clanMember);
    }

    public boolean AddToClan(ClanMember clanMember) {
        if (members.contains(clanMember)) {
            return false;
        }
        members.add(clanMember);
        return true;
    }

    public boolean RemoveFromClan(ClanMember clanMember) {
        if (!members.contains(clanMember)) {
            return false;
        }
        members.remove(clanMember);
        return true;
    }

    public void ClanRequestAction(ClanRequest clanRequest) {
        if (clanRequests.contains(clanRequest)) {
            clanRequests.remove(clanRequest);
        } else {
            clanRequests.add(clanRequest);
        }
    }
}
