package com.groofycode.GroofyCode.dto.Team;

import lombok.Data;
import java.util.List;

@Data
public class TeamDTO {
    private Long id;
    private String name;
    private int membersCount;
    private String creatorUsername;
    private List<MemberDTO> members;
}
