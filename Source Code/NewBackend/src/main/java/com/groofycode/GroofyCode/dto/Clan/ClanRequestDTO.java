package com.groofycode.GroofyCode.dto.Clan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanRequestDTO {
    private Long id;

    private String username;

    private String photoUrl;
}
