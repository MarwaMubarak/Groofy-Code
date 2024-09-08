package com.groofycode.GroofyCode.model.Badge;

import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import lombok.*;

import java.util.List;

@Table(name = "badges", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "photo", "description"})})
@Entity
@NoArgsConstructor
@Getter
@Setter
public class BadgeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "badges_users", joinColumns = @JoinColumn(name = "badge_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserModel> users;
}