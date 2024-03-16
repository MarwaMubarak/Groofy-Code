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
    private Long userId; // Assuming userId instead of User object for simplicity
    @NotBlank(message = "You should enter at least one character")
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private List<LikeDTO> likes;

    public PostDTO() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}