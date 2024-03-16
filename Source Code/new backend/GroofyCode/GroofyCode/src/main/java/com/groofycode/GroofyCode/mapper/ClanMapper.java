package com.groofycode.GroofyCode.mapper;

import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.model.ClanModel;
import com.groofycode.GroofyCode.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class ClanMapper {

    public ClanDTO toDTO(ClanModel clanModel) {
        ClanDTO dto = new ClanDTO();
        dto.setId(clanModel.getId());
        dto.setName(clanModel.getName());
        dto.setBadges(clanModel.getBadges());
        dto.setLeader(clanModel.getLeader());
        dto.setMembers(clanModel.getMembers());
        return dto;
    }

    public ClanModel toModel(ClanDTO clanDTO) {
        ClanModel model = new ClanModel();
       model.setId(clanDTO.getId());
        model.setName(clanDTO.getName());
        model.setBadges(clanDTO.getBadges());
        model.setLeader(clanDTO.getLeader());
        model.setMembers(clanDTO.getMembers());
        return model;
    }

    public List<ClanDTO> toDTOs(List<ClanModel> clanModels) {
        List<ClanDTO> dtos = new ArrayList<>();
        for (int i = 0; i < clanModels.size(); i++) {
            ClanDTO dto = toDTO(clanModels.get(i));
            dtos.add(dto);
        }

        return dtos;
    }

    public List<ClanModel> toModels(List<ClanDTO> clanDTOs) {
        List<ClanModel> models = new ArrayList<>();
        for (int i = 0; i < clanDTOs.size(); i++) {
            ClanModel model = toModel(clanDTOs.get(i));
            models.add(model);
        }
        return models;
    }
}
