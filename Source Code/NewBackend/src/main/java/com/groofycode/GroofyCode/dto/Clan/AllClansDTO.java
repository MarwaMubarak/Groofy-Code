package com.groofycode.GroofyCode.dto.Clan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AllClansDTO {
    private Integer totalClans;
    private List<ClanDTO> clans;
}
