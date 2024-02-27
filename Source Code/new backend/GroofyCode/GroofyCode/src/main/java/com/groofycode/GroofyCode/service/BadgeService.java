package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.model.BadgeModel;
import com.groofycode.GroofyCode.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    public List<BadgeDTO> getAllBadges() {
        List<BadgeModel> badges = badgeRepository.findAll();
        return badges.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BadgeDTO getBadgeById(Long id) {
        Optional<BadgeModel> badgeOptional = badgeRepository.findById(id);
        return badgeOptional.map(this::convertToDTO).orElse(null);
    }

    public BadgeDTO createBadge(BadgeDTO badgeDTO) {

        BadgeModel badge = convertToEntity(badgeDTO);
        badge = badgeRepository.save(badge);
        return convertToDTO(badge);
    }

    public void deleteBadge(Long id) {
        badgeRepository.deleteById(id);
    }

    private BadgeDTO convertToDTO(BadgeModel badge) {
        BadgeDTO badgeDTO = new BadgeDTO();
        badgeDTO.setId(badge.getId());
        badgeDTO.setName(badge.getName());
        badgeDTO.setPhoto(badge.getPhoto());
        badgeDTO.setDescription(badge.getDescription());
        return badgeDTO;
    }

    private BadgeModel convertToEntity(BadgeDTO badgeDTO) {
        BadgeModel badge = new BadgeModel();
        badge.setId(badgeDTO.getId());
        badge.setName(badgeDTO.getName());
        badge.setPhoto(badgeDTO.getPhoto());
        badge.setDescription(badgeDTO.getDescription());
        return badge;
    }
}
