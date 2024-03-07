package com.groofycode.GroofyCode.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
@Getter
@Setter
@NoArgsConstructor
public class TokenInfoDTO {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    private String userAgentText ;

    private String localIpAddress;

    private String remoteIpAddress;
}
