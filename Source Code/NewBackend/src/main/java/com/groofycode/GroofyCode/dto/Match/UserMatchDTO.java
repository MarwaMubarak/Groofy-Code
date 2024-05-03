package com.groofycode.GroofyCode.dto.Match;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class UserMatchDTO {
    private UUID userMatchId;

    private String status;

    private String state;

    private Date createdAt;
}
