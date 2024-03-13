package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;
@Setter
@Getter
@Entity
@Data
@Table(name = "BADGE",uniqueConstraints={@UniqueConstraint(columnNames={"name","photo","description"})})
public class BadgeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String photo;

    @Column(nullable = false)
    private String description;

}
