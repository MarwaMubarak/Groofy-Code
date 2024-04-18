package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.model.BadgeModel;
import com.groofycode.GroofyCode.repository.BadgeRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BadgeService(BadgeRepository badgeRepository, ModelMapper modelMapper) {
        this.badgeRepository = badgeRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<Object> getAllBadges() throws Exception {
        try {
            List<BadgeModel> badges = badgeRepository.findAll();

            List<BadgeDTO> badgeDTOList = badges.stream()
                    .map(bm -> modelMapper.map(bm, BadgeDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Badges retrieved successfully", badgeDTOList));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getBadgeById(Long id) throws Exception {
        try {
            Optional<BadgeModel> badgeOptional = badgeRepository.findById(id);
            if (badgeOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Badge not found", null));
            }
            BadgeDTO badgeDTO = modelMapper.map(badgeOptional.get(), BadgeDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Badge retrieved successfully", badgeDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> createBadge(BadgeDTO badgeDTO) throws Exception {
        try {
            BadgeModel badge = modelMapper.map(badgeDTO, BadgeModel.class);
            Optional<BadgeModel> badgeName = badgeRepository.findByName(badgeDTO.getName());
            Optional<BadgeModel> badgePhoto = badgeRepository.findByPhoto(badgeDTO.getPhoto());
            if (badgeName.isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.unsuccessfulRes("Badge with this name already exists.", null));
            if (badgePhoto.isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.unsuccessfulRes("Badge with this photo already exists.", null));
            badge = badgeRepository.save(badge);
            modelMapper.map(badge, badgeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Badge Created Successfully!", badgeDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> deleteBadge(Long id) throws Exception {
        try {
            Optional<BadgeModel> badgeModel = badgeRepository.findById(id);
            if (badgeModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Badge not found", null));
            badgeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Badge Deleted Successfully!", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> updateBadge(Long id, BadgeDTO badgeDTO) throws Exception {
        try {
            Optional<BadgeModel> badgeModel = badgeRepository.findById(id);
            if (badgeModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Badge Not Found", null));
            }

            Optional<BadgeModel> badgeModelName = badgeRepository.findByName(badgeDTO.getName());
            Optional<BadgeModel> badgeModelPhoto = badgeRepository.findByPhoto(badgeDTO.getPhoto());
            if (badgeModelName.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Badge with this name already exists.", null));
            }
            if (badgeModelPhoto.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Badge with this photo already exists.", null));
            }

            modelMapper.map(badgeDTO, badgeModel.get());
            BadgeModel savedBadge = badgeRepository.save(badgeModel.get());
            modelMapper.map(savedBadge, badgeDTO);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Badge Updated Successfully!", badgeDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}