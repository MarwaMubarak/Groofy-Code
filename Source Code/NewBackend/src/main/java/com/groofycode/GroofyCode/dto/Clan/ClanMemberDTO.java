package com.groofycode.GroofyCode.dto.Clan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClanMemberDTO {
    private String username;
    private String photoUrl;
    private String accountColor;
    private Integer status;
    private String role;
}
