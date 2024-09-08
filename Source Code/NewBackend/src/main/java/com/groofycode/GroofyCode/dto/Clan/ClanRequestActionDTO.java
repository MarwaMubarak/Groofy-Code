package com.groofycode.GroofyCode.dto.Clan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClanRequestActionDTO {
    private Long clanId;
    private Long clanRequestId;
    private String action;
}
