package com.groofycode.GroofyCode.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class PostDTO {
    @NotBlank(message = "You should enter at least one character")
    private String content;
    private List<LikeDTO> likes;
    private Date createdAt;
    private Date updatedAt;
}