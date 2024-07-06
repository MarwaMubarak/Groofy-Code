package com.groofycode.GroofyCode.dto.Friend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendDTO {
    private Long friendId;
    private String photoUrl;
    private String username;
    private String accountColor;
    private Boolean isInvited;
    private Long invitationId;
}
