package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class ClanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "leader", nullable = false)
    private UserModel leader;

    @OneToMany
    @JoinColumn(name = "members")
    private List<UserModel> members;

    @OneToMany
    @JoinColumn(nullable = false)
    private List<BadgeModel> badges;




}
