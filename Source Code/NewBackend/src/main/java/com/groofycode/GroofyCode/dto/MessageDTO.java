package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.model.ClanModel;
import com.groofycode.GroofyCode.model.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private Long userId;

    private String content;
}
