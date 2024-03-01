package com.groofycode.GroofyCode.validator;

import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.model.ClanModel;
import org.springframework.stereotype.Component;

@Component
public class ClanValidator {

    public ClanDTO updateValidator(ClanDTO oldClanDTO, ClanDTO newClanDTO){
        ClanDTO updatedClanDTO = new ClanDTO();
        ///ToDO
        return updatedClanDTO;

    }

    public ClanDTO joinValidator(ClanDTO clanDTO, UserDTO userDTO){

        ClanDTO newClanDTO = new ClanDTO();
        ///ToDO

        return newClanDTO;
    }
    public ClanDTO leaveValidator(ClanDTO clanDTO, UserDTO userDTO){

        ClanDTO newClanDTO = new ClanDTO();
        ///ToDO

        return newClanDTO;
    }

}
