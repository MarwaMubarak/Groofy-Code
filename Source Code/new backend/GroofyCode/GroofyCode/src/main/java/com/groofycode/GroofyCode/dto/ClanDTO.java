package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.dto.User.UserDTO;
import com.groofycode.GroofyCode.model.BadgeModel;
import com.groofycode.GroofyCode.model.UserModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Setter
@Getter
public class ClanDTO {
    private Long id;

    @NotBlank
    @Size(min = 4, max = 100, message = "Name length must be between 4 and 100")
    private String name;

    private UserModel leader;

    private List<UserDTO> members;

    private List<BadgeModel> badges;
}
