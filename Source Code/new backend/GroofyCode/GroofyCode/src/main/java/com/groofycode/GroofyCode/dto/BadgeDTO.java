package com.groofycode.GroofyCode.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BadgeDTO {
    private Long id;

    @NotBlank
    @Size(min = 4, max = 100)
    private String name;

    @NotBlank
    private String photo;

    @NotBlank
    @Size(min = 4, max = 1000)
    private String description;

    public BadgeDTO(Long id, String name, String photo, String description) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
    }
    public BadgeDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
