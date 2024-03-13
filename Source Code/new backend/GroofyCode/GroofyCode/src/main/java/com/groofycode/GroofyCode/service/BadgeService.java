package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.mapper.BadgeMapper;
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
    @Autowired
    private BadgeMapper badgeMapper;

    public List<BadgeDTO> getAllBadges() {
        List<BadgeModel> badges = badgeRepository.findAll();
        return badges.stream()
                .map(badgeModel -> badgeMapper.toDTO(badgeModel))
                .collect(Collectors.toList());
    }

    public BadgeDTO getBadgeById(Long id) {
        Optional<BadgeModel> badgeOptional = badgeRepository.findById(id);
        return badgeOptional.map(badgeModel -> badgeMapper.toDTO(badgeModel)).orElse(null);
    }

    public BadgeDTO createBadge(BadgeDTO badgeDTO) {

        System.out.println("aaaaaaaaaaaaaiiiiiiiiiiiiiiiiiiiiiiiii");
        BadgeModel badge = badgeMapper.toModel(badgeDTO);
        badge = badgeRepository.save(badge);
        return badgeMapper.toDTO(badge);
    }

    public void deleteBadge(Long id) {
        badgeRepository.deleteById(id);
    }

}
