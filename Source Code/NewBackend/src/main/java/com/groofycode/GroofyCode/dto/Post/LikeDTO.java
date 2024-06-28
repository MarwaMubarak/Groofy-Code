package com.groofycode.GroofyCode.dto.Post;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class LikeDTO {
    private Long userId;
    private Date likedAt;
}
