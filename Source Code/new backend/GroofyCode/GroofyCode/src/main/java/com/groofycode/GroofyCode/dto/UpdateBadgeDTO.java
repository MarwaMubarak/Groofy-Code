package com.groofycode.GroofyCode.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateBadgeDTO {

    private Long id;

    @Size(min = 4, max = 100,message =  "Name length must be between 4 and 100")
    private String name;

    @Size(min = 4, max = 100,message =  "Photo length must be between 4 and 100")
    private String photo;

    @Size(min = 4, max = 100, message =  "Description length must be between 4 and 100")
    private String description;


    public UpdateBadgeDTO(){}
    public UpdateBadgeDTO(Long id, String name, String photo, String description) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
    }

}
