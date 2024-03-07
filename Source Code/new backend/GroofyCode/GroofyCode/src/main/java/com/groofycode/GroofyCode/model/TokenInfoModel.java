package com.groofycode.GroofyCode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table
public class TokenInfoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(length = 800)
    private String accessToken;

    @NotBlank
    @Column(length = 800)
    private String refreshToken;

    private String userAgentText ;

    private String localIpAddress;

    private String remoteIpAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID",referencedColumnName = "ID")
    private UserModel user;

    public TokenInfoModel(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
