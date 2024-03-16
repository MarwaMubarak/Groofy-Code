package com.groofycode.GroofyCode.service;


import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.mapper.ClanMapper;
import com.groofycode.GroofyCode.mapper.UserMapper;
import com.groofycode.GroofyCode.model.ClanModel;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.ClanRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClanService {

    @Autowired
    private ClanRepository clanRepository;

    @Autowired
    private ClanMapper clanMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public ResponseEntity<?> getAll() {
        try {
            List<ClanModel> clans = clanRepository.findAll();

            List<ClanDTO> clansDTOList = clans.stream()
                    .map(clanModel -> clanMapper.toDTO(clanModel)).toList();

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Done Successfully!", clansDTOList));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }

    }

    public ResponseEntity<?> getById(Long id) {
        try {
            Optional<ClanModel> clanOptional = clanRepository.findById(id);
            if (clanOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Not Found", null));
            }
            ClanDTO clanDTO = clanOptional.map(clanModel -> clanMapper.toDTO(clanModel)).orElse(null);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Done Successfully!", clanDTO));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }

    }

    public ResponseEntity<?> create(ClanDTO clanDTOBody) {

        try {

            UserModel currentUser = userService.getCurrentUser();
            clanDTOBody.setLeader(currentUser);
            ClanModel clan = clanMapper.toModel(clanDTOBody);
            // check if same name
            Optional<ClanModel> clanByName = clanRepository.findByName(clan.getName());
            if (clanByName.isPresent())//found
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("The Name Should Be Unique!", null));
            Optional<ClanModel> clanByLeader = clanRepository.findByLeader(currentUser);
            if(clanByLeader.isPresent())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You already have Created a Clan!", null));

            System.out.println("offffffffffff1"+ clan.getName()+clan.getId());

            ClanModel savedClan = clanRepository.save(clan);
            System.out.println("offffffffffff2");

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Created Successfully!", clanMapper.toDTO(savedClan)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(id);
            if (clanModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Not Found", null));

            if(clanModel.get().getLeader()!=userService.getCurrentUser())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Not Allowed!", null));

            clanRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Deleted Successfully!", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }

    }

    public ResponseEntity<?> updateName(Long clanId, String name) {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);


            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Not Found", null));
            }

            if(clanModel.get().getLeader()!=userService.getCurrentUser())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Not Allowed!", null));


            Optional<ClanModel> clanModelName = clanRepository.findByName(name);
            if (clanModelName.isPresent())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Name Should Be Unique", null));

            ClanDTO updatedClanDTO = clanMapper.toDTO(clanModel.get());
            updatedClanDTO.setName(name);
            ClanModel updatedBadgeModel = clanRepository.save(clanMapper.toModel(updatedClanDTO));

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Updated Successfully!", clanMapper.toDTO(updatedBadgeModel)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }
    }

    public ResponseEntity<?> joinClan(Long clanId) {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);
            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan Not Found", null));
            }

            ClanDTO updatedClanDTO = clanMapper.toDTO(clanModel.get());
            Optional<UserModel> member = userRepository.findById(userService.getCurrentUser().getId());
            if(member.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.successfulRes("Member Not Found!",null));
            }
            boolean ok = updatedClanDTO.addMember(member.get().getId());
            if(!ok){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.successfulRes("Already Member!",null));

            }
            ClanModel updatedBadgeModel = clanRepository.save(clanMapper.toModel(updatedClanDTO));

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Joined Successfully!", clanMapper.toDTO(updatedBadgeModel)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }
    }

    public ResponseEntity<?> leaveClan(Long clanId) {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);
            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan Not Found", null));
            }

            ClanDTO updatedClanDTO = clanMapper.toDTO(clanModel.get());
            Optional<UserModel> member = userRepository.findById(userService.getCurrentUser().getId());
            if(member.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.successfulRes("Member Not Found!",null));
            }
            boolean ok = updatedClanDTO.removeMember(member.get().getId());
            if(!ok){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.successfulRes("Already Not Member!",null));

            }
            ClanModel updatedBadgeModel = clanRepository.save(clanMapper.toModel(updatedClanDTO));

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Leaved Successfully!", clanMapper.toDTO(updatedBadgeModel)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.unsuccessfulRes("An internal server error occurred", null));

        }
    }


}
