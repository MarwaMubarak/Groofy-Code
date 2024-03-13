package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CLAN",uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class ClanModel {

    public ClanModel() {}


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @OneToOne
    @JoinColumn(name = "leader", nullable = false)
    private UserModel leader;

    @OneToMany(mappedBy = "clan")
    private List<UserModel> members;

    @Column(nullable = false)
    private String current_rank="Metal";




}
