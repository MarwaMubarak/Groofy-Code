package com.groofycode.GroofyCode.dto.Clan;

import com.groofycode.GroofyCode.dto.User.UserDTO;
import com.groofycode.GroofyCode.model.BadgeModel;
import com.groofycode.GroofyCode.model.User.UserModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ClanDTO {
    private Long id;

    @NotBlank
    @Size(min = 4, max = 100, message = "Name length must be between 4 and 100")
    private String name;

    private Integer membersCount;

    private Integer wins;

    private Integer losses;

    private Integer totalMatches;

    private Integer worldRank;

    private Integer requestStatus;

    private List<ClanMemberDTO> members;

    private List<BadgeModel> badges;
}
