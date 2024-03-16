package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(name = "CLAN", uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class ClanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "leader", nullable = false)
    private UserModel leader;

    @Column
    private List<Long> members;

    @OneToMany
    @JoinColumn(nullable = false)
    private List<BadgeModel> badges;


    public ClanModel(){
        badges=new ArrayList<>();
        members=new ArrayList<>();
    }


}
