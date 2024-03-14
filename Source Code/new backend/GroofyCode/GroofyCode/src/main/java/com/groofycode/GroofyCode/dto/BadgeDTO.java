package com.groofycode.GroofyCode.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Size(min = 4, max = 100, message =  "Description length must be between 4 and 100")
    private String description;


    public BadgeDTO(){}
    public BadgeDTO(Long id, String name, String photo, String description) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
    }


//    public Pair<HashMap<String,List<String>>,BadgeDTO> updateBadge(BadgeDTO badgeDTO){
//
//        List<String> errors = new ArrayList<>();
//
//        if(badgeDTO.getDescription().length()<=100 && badgeDTO.getDescription().length()>=4){
//            this.description = badgeDTO.getDescription();
//        }else {
//            errors.add( "Description length must be between 4 and 100");
//        }
//
//        if(badgeDTO.getPhoto().length()>=4){
//            this.photo = badgeDTO.getPhoto();
//        }
//
//
//        if(badgeDTO.getPhoto().length()<=100 && badgeDTO.getName().length()>=4){
//            this.name = badgeDTO.getName();
//        }else{
//            errors.add( "Name length must be between 4 and 100");
//
//        }
//
//        HashMap errMap = new HashMap<>();
//        errMap.put("errors",errors);
//
//        return (errMap, this)
//    }
}
