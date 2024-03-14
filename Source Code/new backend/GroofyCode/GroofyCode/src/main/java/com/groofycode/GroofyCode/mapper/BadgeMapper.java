package com.groofycode.GroofyCode.mapper;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.dto.UpdateBadgeDTO;
import com.groofycode.GroofyCode.model.BadgeModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BadgeMapper {

    public BadgeDTO toDTO(BadgeModel badgeModel) {
        if (badgeModel == null) {
            return null;
        }

        BadgeDTO dto = new BadgeDTO();
        dto.setId(badgeModel.getId());
        dto.setName(badgeModel.getName());
        dto.setPhoto(badgeModel.getPhoto());
        dto.setDescription(badgeModel.getDescription());
        return dto;
    }

    public BadgeModel toModel(BadgeDTO badgeDTO) {
        if (badgeDTO == null) {
            return null;
        }

        BadgeModel model = new BadgeModel();
        model.setId(badgeDTO.getId());
        model.setName(badgeDTO.getName());
        model.setPhoto(badgeDTO.getPhoto());
        model.setDescription(badgeDTO.getDescription());
        return model;
    }

    public List<BadgeDTO> toDTOs(List<BadgeModel> badgeModels) {
        if (badgeModels == null) {
            return null;
        }

        List<BadgeDTO> dtos = new ArrayList<>();
        for (BadgeModel badgeModel : badgeModels) {
            dtos.add(toDTO(badgeModel));
        }
        return dtos;
    }

    public List<BadgeModel> toModels(List<BadgeDTO> badgeDTOs) {
        if (badgeDTOs == null) {
            return null;
        }

        List<BadgeModel> models = new ArrayList<>();
        for (BadgeDTO badgeDTO : badgeDTOs) {
            models.add(toModel(badgeDTO));
        }
        return models;
    }

    public BadgeDTO updateBadge(UpdateBadgeDTO updateBadgeDTO, BadgeDTO badgeDTO){
        if(updateBadgeDTO.getName()!=null){
            badgeDTO.setName(updateBadgeDTO.getName());
        }
        if(updateBadgeDTO.getDescription()!=null){
            badgeDTO.setDescription(updateBadgeDTO.getDescription());

        }
        if(updateBadgeDTO.getPhoto()!=null){
            badgeDTO.setPhoto(updateBadgeDTO.getPhoto());
        }
        return badgeDTO;
    }
}
