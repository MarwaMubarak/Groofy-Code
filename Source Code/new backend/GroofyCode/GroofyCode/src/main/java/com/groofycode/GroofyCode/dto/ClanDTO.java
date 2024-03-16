package com.groofycode.GroofyCode.dto;

import com.groofycode.GroofyCode.model.BadgeModel;
import com.groofycode.GroofyCode.model.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanDTO {


    private Long id;

    @NotBlank
    @Size(min = 4,max = 100,message = "Name length must be between 4 and 100")
    private String name;

    private UserModel leader;

    private List<UserModel> members=new ArrayList<>();

    private List<BadgeModel> badges=new ArrayList<>();

    public boolean addMember(UserModel member){
        int index = members.indexOf(member);
        if(index!=-1)
            return false;
        members.add(member);
        return true;
    }

    public boolean removeMember(UserModel member){
        int index = members.indexOf(member);
        if(index==-1)
            return false;
        members.remove(member);
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }
}
