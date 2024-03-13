package com.groofycode.GroofyCode.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@Data
public class BadgeDTO {



    private Long id;

    @NotNull(message = "name is mandatory")
    @NotBlank(message = "name is mandatory")
    @Min(value = 4, message =  " name length must be between 4 and 100")
    @Max(value = 100, message =  " name length must be between 4 and 100")    private String name;

    @NotBlank(message = "photo is mandatory")
    @NotNull(message = "photo is mandatory")
    private String photo;

    @NotBlank(message = "description is mandatory")
    @NotNull(message = "description is mandatory")
    @Min(value = 4, message =  " description length must be between 4 and 100")
    @Max(value = 100, message =  " description length must be between 4 and 100")

    private String description;

    public BadgeDTO(){}
    public BadgeDTO(Long id, String name, String photo, String description) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
    }
}
