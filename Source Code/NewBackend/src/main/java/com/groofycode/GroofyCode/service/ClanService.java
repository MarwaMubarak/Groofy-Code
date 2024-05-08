package com.groofycode.GroofyCode.service;


import com.groofycode.GroofyCode.dto.Clan.*;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Clan.ClanModel;
import com.groofycode.GroofyCode.model.Clan.ClanMember;
import com.groofycode.GroofyCode.model.Clan.ClanRequest;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Clan.ClanMembersRepository;
import com.groofycode.GroofyCode.repository.Clan.ClanRepository;
import com.groofycode.GroofyCode.repository.Clan.ClanRequestRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClanService {
    private final ClanRepository clanRepository;
    private final ClanMembersRepository clanMembersRepository;
    private final ClanRequestRepository clanRequestRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClanService(ClanRepository clanRepository, ClanMembersRepository clanMembersRepository, ClanRequestRepository clanRequestRepository
            , UserRepository userRepository, ModelMapper modelMapper) {
        this.clanRepository = clanRepository;
        this.clanMembersRepository = clanMembersRepository;
        this.clanRequestRepository = clanRequestRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private List<ClanDTO> getClanDtoList(List<ClanModel> clanModels, UserModel currentUser) {
        return clanModels.stream().map(clan -> {
            clan.setMembers(null);
            ClanDTO clanDTO = modelMapper.map(clan, ClanDTO.class);
            Integer membersCnt = clanRepository.countMembersById(clan.getId());
            clanDTO.setMembersCount(membersCnt);
            if (currentUser.getClanRequest() != null && currentUser.getClanRequest().getClan().getId().equals(clan.getId())) {
                clanDTO.setRequestStatus(0); // pending --> able to cancel request
            } else if (currentUser.getClanRequest() == null && currentUser.getClanMember() == null) {
                clanDTO.setRequestStatus(1); // able to request
            } else if (currentUser.getClanMember().getClan().getId().equals(clan.getId())) {
                clanDTO.setRequestStatus(2); // member --> able to view clan
            } else {
                clanDTO.setRequestStatus(3); // forbidden to cancel request and view clan
            }
            return clanDTO;
        }).toList();
    }

    public ResponseEntity<Object> getAll(Integer page, Integer size) throws Exception {
        try {
            AllClansDTO allClansDTO = new AllClansDTO();
            Integer totalClans = clanRepository.countClans();
            allClansDTO.setTotalClans(totalClans);

            if (totalClans != 0) {
                UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                UserModel currentUser = userRepository.fetchUserWithClanRequestByUsername(userInfo.getUsername());
                if (size == null) size = 10;
                if (page == null) page = 0;
                PageRequest pageRequest = PageRequest.of(page, size);
                List<ClanModel> clans = clanRepository.findAll(pageRequest).getContent();
                List<ClanDTO> clansDTOList = getClanDtoList(clans, currentUser);
                allClansDTO.setClans(clansDTOList);
            } else {
                allClansDTO.setClans(new ArrayList<>());
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Clans retrieved successfully", allClansDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getClan() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());
            if (currentUser.getClanMember() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("You are not a member of any clan.", null));
            }
            ClanModel clanModel = currentUser.getClanMember().getClan();

            PageRequest pageRequest = PageRequest.of(0, 8);
            List<ClanMember> clanMembers = clanMembersRepository.findByClanId(clanModel.getId(), pageRequest).getContent();
            List<ClanMemberDTO> clanMemberDTOS = clanMembers.stream().map(member -> {
                UserModel userModel = member.getUser();
                return new ClanMemberDTO(userModel.getUsername(), userModel.getPhotoUrl(), userModel.getStatus(), member.getRole());
            }).toList();

            ClanDTO clanDTO = modelMapper.map(clanModel, ClanDTO.class);
            clanDTO.setMembers(clanMemberDTOS);
            clanDTO.setMembersCount(clanRepository.countMembersById(clanModel.getId()));
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Clan retrieved successfully", clanDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> searchForClan(String query, Integer page, Integer size) throws Exception {
        try {
            AllClansDTO allClansDTO = new AllClansDTO();
            Integer totalSearchedClans = clanRepository.countSearchedClans(query);
            allClansDTO.setTotalClans(totalSearchedClans);

            if (totalSearchedClans != 0) {
                UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                UserModel currentUser = userRepository.fetchUserWithClanRequestByUsername(userInfo.getUsername());
                if (page == null) page = 0;
                if (size == null) size = 10;
                PageRequest pageRequest = PageRequest.of(page, size);
                List<ClanModel> clans = clanRepository.findByNameContainingIgnoreCase(query, pageRequest).getContent();
                List<ClanDTO> clansDTOList = getClanDtoList(clans, currentUser);
                allClansDTO.setClans(clansDTOList);
            } else {
                allClansDTO.setClans(new ArrayList<>());
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Clans retrieved successfully", allClansDTO));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> create(ClanDTO clanDTO) throws Exception {
        try {
            ClanModel existingClan = clanRepository.findByNameIgnoreCase(clanDTO.getName());
            if (existingClan != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Clan with this name already exists.", null));
            }
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());
            if (currentUser.getClanMember() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("You are already a member of a clan.", null));
            }

            ClanModel clan = new ClanModel();
            clan.setName(clanDTO.getName());
            clan.setLeader(currentUser.getUsername());
            ClanMember clanMember = new ClanMember(currentUser, clan, "leader");
            currentUser.setClanMember(clanMember);
            clan.AddToClan(currentUser.getClanMember());
            ClanModel savedClan = clanRepository.save(clan);

            modelMapper.map(savedClan, clanDTO);
            List<ClanMemberDTO> clanMemberDTOS = savedClan.getMembers().stream().map(member -> {
                UserModel userModel = member.getUser();
                return new ClanMemberDTO(userModel.getUsername(), userModel.getPhotoUrl(), userModel.getStatus(), member.getRole());
            }).toList();

            clanDTO.setMembers(clanMemberDTOS);
            clanDTO.setMembersCount(1);

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Clan created successfully!", clanDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> delete(Long id) throws Exception {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(id);
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());

            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan not found", null));
            }

            if (!currentUser.getClanMember().getRole().equals("leader")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Action not allowed", null));
            }

            clanModel.get().getMembers().forEach(member -> member.getUser().setClanMember(null));
            clanRepository.delete(clanModel.get());
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Clan deleted successfully", null));
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

            if (!currentUser.getClanMember().getRole().equals("leader")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("Action not allowed.", null));
            }

            ClanModel clanModelName = clanRepository.findByNameIgnoreCase(name);
            if (clanModelName != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("Clan with this name already exists.", null));
            }

            clanModel.get().setName(name);
            ClanModel updatedClan = clanRepository.save(clanModel.get());
            updatedClan.setMembers(null);
            ClanDTO clanDTO = modelMapper.map(updatedClan, ClanDTO.class);
            clanDTO.setMembersCount(clanRepository.countMembersById(clanId));
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Clan name updated successfully.", clanDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> getClanRequests(Integer page, Integer size) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());
            if (currentUser.getClanMember() == null || !currentUser.getClanMember().getRole().equals("leader")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("Action not allowed", null));
            }

            AllClanRequestsDTO allClanRequestsDTO = new AllClanRequestsDTO();
            Integer clanRequestsCount = clanRequestRepository.countClanRequestByClanId(currentUser.getClanMember().getClan().getId());
            allClanRequestsDTO.setTotalRequests(clanRequestsCount);

            if (clanRequestsCount != 0) {
                if (page == null) page = 0;
                if (size == null) size = 10;
                PageRequest pageRequest = PageRequest.of(page, size);
                List<ClanRequest> clanRequests = clanRequestRepository.fetchByClanId(currentUser.getClanMember().getClan().getId(), pageRequest).getContent();
                List<ClanRequestDTO> clanRequestDTOS = clanRequests.stream().map(clanRequest -> {
                    UserModel userModel = clanRequest.getUser();
                    return new ClanRequestDTO(clanRequest.getId(), userModel.getUsername(), userModel.getPhotoUrl());
                }).toList();
                allClanRequestsDTO.setRequests(clanRequestDTOS);
            } else {
                allClanRequestsDTO.setRequests(new ArrayList<>());
            }

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Clan requests retrieved successfully", allClanRequestsDTO));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> clanRequest(Long clanId) throws Exception {
        try {
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);
            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan not found", null));
            }

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());
            if (clanModel.get().IsMember(currentUser.getClanMember())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are already a member in this clan.", null));
            }

            if (currentUser.getClanMember() != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("You are already assigned to a clan", null));
            }

            if (currentUser.getClanRequest() != null && !currentUser.getClanRequest().getClan().getId().equals(clanId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.unsuccessfulRes("You already sent a request to another clan", null));
            }

            ClanRequest existingRequest = clanRequestRepository.findByUserIdAndClanId(currentUser.getId(), clanId);
            if (existingRequest != null) {
                currentUser.setClanRequest(null);
                clanModel.get().ClanRequestAction(existingRequest);
                clanRequestRepository.delete(existingRequest);
                return ResponseEntity.ok(ResponseUtils.successfulRes("Request cancelled successfully", null));
            }
            ClanRequest clanRequest = new ClanRequest(clanModel.get(), currentUser);
            currentUser.setClanRequest(clanRequest);
            clanModel.get().ClanRequestAction(clanRequest);
            clanRequestRepository.save(clanRequest);
            return ResponseEntity.ok(ResponseUtils.successfulRes("Request sent successfully", null));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> clanRequestAction(ClanRequestActionDTO clanRequestActionDTO) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());
            Optional<ClanModel> clanModel = clanRepository.findById(clanRequestActionDTO.getClanId());

            if (clanModel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Clan not found", null));
            }

            if (currentUser.getClanMember() == null || !currentUser.getClanMember().getClan().equals(clanModel.get()) ||
                    !currentUser.getClanMember().getRole().equals("leader")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.unsuccessfulRes("Action not allowed", null));
            }

            ClanRequest clanRequest = clanRequestRepository.findById(clanRequestActionDTO.getClanRequestId()).orElse(null);

            if (clanRequest == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("Request not found", null));
            }

            if (!Objects.equals(clanRequest.getClan().getId(), clanModel.get().getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("Request is not for this clan", null));
            }

            if (clanRequestActionDTO.getAction().equals("acc")) {
                if (clanModel.get().IsMember(clanRequest.getUser().getClanMember())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("This is user is already a member", null));
                }

                ClanMember clanMember = new ClanMember(clanRequest.getUser(), clanModel.get(), "member");
                clanRequest.getUser().setClanMember(clanMember);
                clanModel.get().AddToClan(clanMember);

                clanModel.get().ClanRequestAction(clanRequest);
                clanRequest.getUser().setClanRequest(null);
                clanRequest.setClan(null);
                clanRequest.setUser(null);
                clanMembersRepository.save(clanMember);
                clanRequestRepository.delete(clanRequest);

                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Request accepted successfully", null));
            } else {
                clanRequest.getUser().setClanRequest(null);
                clanRequest.setUser(null);
                clanRequest.setClan(null);
                clanRequestRepository.delete(clanRequest);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Request rejected successfully", null));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<Object> leaveClan() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel currentUser = userRepository.fetchUserWithClanMemberByUsername(userInfo.getUsername());

            if (currentUser.getClanMember() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.unsuccessfulRes("You are not a member of any clan.", null));
            }

            ClanModel clanModel = currentUser.getClanMember().getClan();

            if (currentUser.getClanMember().getRole().equals("leader") && clanModel.getMembers().size() > 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You can't leave the clan, you are the leader.", null));
            }

            if (currentUser.getClanMember().getRole().equals("leader") && clanModel.getMembers().size() == 1) {
                currentUser.setClanMember(null);
                clanRepository.delete(clanModel);
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Clan deleted successfully.", null));
            }

            boolean isRemoved = clanModel.RemoveFromClan(currentUser.getClanMember());
            if (!isRemoved) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes("You are not a member in this clan.", null));
            }
            ClanMember clanMember = currentUser.getClanMember();
            currentUser.setClanMember(null);
            clanMembersRepository.delete(clanMember);
            clanRepository.save(clanModel);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Left clan successfully.", null));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
