package com.groofycode.GroofyCode.dto;


import jakarta.validation.constraints.*;
import lombok.*;
import org.antlr.v4.runtime.misc.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BadgeDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 4, max = 100,message =  "Name length must be between 4 and 100")
    private String name;

    @NotBlank(message = "Photo is required")
    private String photo;

    @NotBlank(message = "Description is required")
    @Size(min = 4, max = 100, message =  "Description length must be between 4 and 100")
    private String description;



}
