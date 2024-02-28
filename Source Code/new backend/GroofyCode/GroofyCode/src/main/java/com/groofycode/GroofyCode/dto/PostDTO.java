package com.groofycode.GroofyCode.dto;
import java.util.Date;
import java.util.List;

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

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }
}
