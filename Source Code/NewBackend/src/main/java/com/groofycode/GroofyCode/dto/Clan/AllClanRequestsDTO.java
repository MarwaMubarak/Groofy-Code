package com.groofycode.GroofyCode.dto.Clan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class AllClanRequestsDTO {
    private Integer totalRequests;
    private List<ClanRequestDTO> requests;
}
