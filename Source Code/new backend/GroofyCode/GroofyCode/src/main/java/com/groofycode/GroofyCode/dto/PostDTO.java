package com.groofycode.GroofyCode.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class PostDTO {
    private Long id;
    private Long postUserId;
    @NotBlank(message = "You should enter at least one character")
    private String content;
    private Integer likesCnt;
    private Integer isLiked;
    private Date createdAt;
    private Date updatedAt;
}