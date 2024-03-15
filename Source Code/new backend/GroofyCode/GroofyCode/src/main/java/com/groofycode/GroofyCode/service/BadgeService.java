package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.dto.UpdateBadgeDTO;
import com.groofycode.GroofyCode.mapper.BadgeMapper;
import com.groofycode.GroofyCode.model.BadgeModel;
import com.groofycode.GroofyCode.repository.BadgeRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private  UserService userService;

    @Autowired
    private BadgeMapper badgeMapper;

    public ResponseEntity<?> getAllBadges() {
        try {
            List<BadgeModel> badges = badgeRepository.findAll();

            List<BadgeDTO>badgeDTOList=badges.stream()
                    .map(badgeModel -> badgeMapper.toDTO(badgeModel))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Done Successfully!",badgeDTOList));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }

    }

    public ResponseEntity<?> getBadgeById(Long id) {
        try{
            Optional<BadgeModel> badgeOptional = badgeRepository.findById(id);
            if (badgeOptional.isEmpty()) {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("This Badge Id Not Found",null));
            }
            BadgeDTO badgeDTO = badgeOptional.map(badgeModel -> badgeMapper.toDTO(badgeModel)).orElse(null);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Done Successfully!",badgeDTO));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }

    }

    public ResponseEntity<?> createBadge(BadgeDTO badgeDTO) {

        try{

            BadgeModel badge = badgeMapper.toModel(badgeDTO);
            badge.setCreatedBy(userService.getCurrentUser());
            // check if same name or photo
            Optional<BadgeModel> badgeName = badgeRepository.findByName(badgeDTO.getName());
            Optional<BadgeModel> badgePhoto = badgeRepository.findByPhoto(badgeDTO.getPhoto());
            if (badgeName.isPresent())//found
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("The Name Should Be Unique!",null));
            if(badgePhoto.isPresent())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("The Photo Should Be Unique!",null));

            BadgeModel savedBadge = badgeRepository.save(badge);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Created Successfully!", badgeMapper.toDTO(savedBadge)));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }
    }

    public ResponseEntity<?> deleteBadge(Long id) {
        try {
            Optional<BadgeModel> badgeModel = badgeRepository.findById(id);
            if (badgeModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("This Badge Id Not Found",null));

            badgeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Deleted Successfully!",null));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }

    }

    public ResponseEntity<?> updateBadge(Long id, UpdateBadgeDTO badgeDTO){
        try {
            Optional<BadgeModel> badgeModel = badgeRepository.findById(id);
            if(badgeModel.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Badge Not Found",null));
            }
            Optional<BadgeModel> badgeModelName = badgeRepository.findByName(badgeDTO.getName());
            Optional<BadgeModel> badgeModelPhoto = badgeRepository.findByPhoto(badgeDTO.getPhoto());
            if(badgeModelName.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Name Should Be Unique",null));
            }
            if(badgeModelPhoto.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Photo Should Be Unique",null));
            }
            BadgeDTO updatedBadgeDTO = badgeMapper.updateBadge(badgeDTO,badgeMapper.toDTO(badgeModel.get()));

            BadgeModel updatedBadgeModel = badgeRepository.save(badgeMapper.toModel(updatedBadgeDTO));

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Updated Successfully!",badgeMapper.toDTO(updatedBadgeModel)));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }
    }

}
