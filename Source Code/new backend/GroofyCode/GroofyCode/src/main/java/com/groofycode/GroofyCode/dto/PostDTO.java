package com.groofycode.GroofyCode.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class PostDTO {
    private Long id;
    private Long userId; // Assuming userId instead of User object for simplicity
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private List<Long> likes; // Assuming likes as list of user IDs for simplicity

    // Constructors, getters, setters, and other methods

    public PostDTO() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
