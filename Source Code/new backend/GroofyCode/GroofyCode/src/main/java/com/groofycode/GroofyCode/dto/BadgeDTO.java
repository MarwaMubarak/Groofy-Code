package com.groofycode.GroofyCode.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class BadgeDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 4, max = 100,message =  "Name length must be between 4 and 100")
    private String name;

    @NotBlank(message = "Photo is required")
    private String photo;

    @NotBlank(message = "Description is required")
    @Size(min = 4, max = 100, message =  " Description length must be between 4 and 100")
    private String description;


    public BadgeDTO(){}
    public BadgeDTO(Long id, String name, String photo, String description) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
    }
}
