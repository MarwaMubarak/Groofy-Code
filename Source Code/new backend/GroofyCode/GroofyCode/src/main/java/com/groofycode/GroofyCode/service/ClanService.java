package com.groofycode.GroofyCode.service;


import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.ClanModel;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.ClanRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClanService {
    private final ClanRepository clanRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClanService(ClanRepository clanRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.clanRepository = clanRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<Object> getAll() throws Exception {
        try {
            List<ClanModel> clans = clanRepository.findAll();
            List<ClanDTO> clansDTOList = clans.stream().map(cm -> modelMapper.map(cm, ClanDTO.class)).toList();
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Done Successfully!", clansDTOList));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getById(Long id) throws Exception {
        try {
            Optional<ClanModel> clanOptional = clanRepository.findById(id);
            if (clanOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Not Found", null));
            }
            ClanDTO clanDTO = modelMapper.map(clanOptional.get(), ClanDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Done Successfully!", clanDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> create(ClanDTO clanDTOBody) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
            clanDTOBody.setLeader(currentUser);
            ClanModel clan = modelMapper.map(clanDTOBody, ClanModel.class);
            // check if same name
            Optional<ClanModel> clanByName = clanRepository.findByName(clan.getName());
            if (clanByName.isPresent())//found
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Clan with this name already exists.", null));
            Optional<ClanModel> clanByLeader = clanRepository.findByLeader(currentUser);
            if (clanByLeader.isPresent())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You already have created a clan.", null));

            ClanModel savedClan = clanRepository.save(clan);
            modelMapper.map(savedClan, clanDTOBody);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Clan Created Successfully!", clanDTOBody));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> delete(Long id) throws Exception {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(id);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            if (clanModel.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Not Found", null));

            if (clanModel.get().getLeader() != currentUser)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Not Allowed!", null));

            clanRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Deleted Successfully!", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> updateName(Long clanId, String name) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);

            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan not found.", null));
            }

            if (clanModel.get().getLeader() != currentUser) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("Action not allowed.", null));
            }

            Optional<ClanModel> clanModelName = clanRepository.findByName(name);
            if (clanModelName.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Clan with this name already exists.", null));
            }

            clanModel.get().setName(name);
            ClanModel updatedClan = clanRepository.save(clanModel.get());
            ClanDTO clanDTO = modelMapper.map(updatedClan, ClanDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Updated Successfully!", clanDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> joinClan(Long clanId) throws Exception {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());

            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan Not Found", null));
            }

            Optional<UserModel> member = userRepository.findById(currentUser.getId());

            if (member.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Member Not Found!", null));
            }
            boolean isAdded = clanModel.get().addToClan(member.get());
            if (!isAdded) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("User is already a member.", null));
            }

            clanRepository.save(clanModel.get());
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("User joined the clan successfully.", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<?> leaveClan(Long clanId) throws Exception {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.findByUsername(userInfo.getUsername());
            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan Not Found.", null));
            }

            Optional<UserModel> member = userRepository.findById(currentUser.getId());
            if (member.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Member Not Found.", null));
            }

            boolean isRemoved = clanModel.get().removeFromClan(member.get());
            if (!isRemoved) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("User is not a member in this clan.", null));
            }
            clanRepository.save(clanModel.get());
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Left clan successfully.", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
