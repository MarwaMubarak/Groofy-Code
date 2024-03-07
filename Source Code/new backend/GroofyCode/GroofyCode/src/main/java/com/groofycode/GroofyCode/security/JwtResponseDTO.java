package com.groofycode.GroofyCode.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JwtResponseDTO {
    private String accessToken;
    private String refreshToken;
}
