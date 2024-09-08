package com.groofycode.GroofyCode.service.Match;

import com.groofycode.GroofyCode.dto.Match.MatchDTO;
import com.groofycode.GroofyCode.dto.Match.UserMatchDTO;
import com.groofycode.GroofyCode.dto.User.UserInfo;
import com.groofycode.GroofyCode.model.Match.Match;
import com.groofycode.GroofyCode.model.Match.UserMatch;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.repository.Match.MatchRepository;
import com.groofycode.GroofyCode.repository.Match.UserMatchRepository;
import com.groofycode.GroofyCode.repository.UserRepository;
import com.groofycode.GroofyCode.utilities.MatchStatusMapper;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final UserMatchRepository userMatchRepository;
    private final UserRepository userRepository;
    private final MatchStatusMapper matchStatusMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public MatchService(MatchRepository matchRepository, UserMatchRepository userMatchRepository,
                        UserRepository userRepository, MatchStatusMapper matchStatusMapper, ModelMapper modelMapper) {
        this.matchRepository = matchRepository;
        this.userMatchRepository = userMatchRepository;
        this.userRepository = userRepository;
        this.matchStatusMapper = matchStatusMapper;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<Object> getUserMatches(Integer page, Integer size) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            PageRequest pageRequest = PageRequest.of(page, size);
            List<UserMatch> userMatches = userMatchRepository.findMatchesByUsername(userModel.getUsername(), pageRequest).getContent();
            List<UserMatchDTO> userMatchDTOS = userMatches.stream().map(userMatch -> {
                UserMatchDTO userMatchDTO = modelMapper.map(userMatch, UserMatchDTO.class);
                userMatchDTO.setStatus(matchStatusMapper.getStatusIntToString().get(userMatch.getStatus()));
                userMatchDTO.setState(matchStatusMapper.getStateIntToString().get(userMatch.getState()));
                userMatchDTO.setCreatedAt(userMatch.getMatch().getCreatedAt());
                return userMatchDTO;
            }).toList();
            return ResponseEntity.ok(ResponseUtils.successfulRes("Retrieved matches successfully", userMatchDTOS));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getCurrentMatch() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            if (userModel.getStatus() == 0) {
                return ResponseEntity.badRequest().body(ResponseUtils.unsuccessfulRes("You aren't enrolled in any matches currently.", null));
            }
            UserMatch userMatch = userMatchRepository.fetchUserMatchByUsername(userModel.getUsername());
            MatchDTO matchDTO = new MatchDTO();
            matchDTO.setMatchId(userMatch.getMatch().getMatchId());
            matchDTO.setStatus(matchStatusMapper.getStatusIntToString().get(userMatch.getStatus()));
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Match found", matchDTO));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> createSoloMatch() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            if (userModel.getStatus() == 1) {
                return ResponseEntity.badRequest().body(ResponseUtils.unsuccessfulRes("You are already in a match", null));
            }
            Match match = new Match();
            UserMatch userMatch = new UserMatch();
            userMatch.setUserModel(userModel);
            match.AddToMatch(userMatch);
            match.setDuration(20);
            userModel.setStatus(1);
            userMatch.setMatch(match);
            matchRepository.save(match);
            MatchDTO matchDTO = new MatchDTO();
            matchDTO.setMatchId(match.getMatchId());
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.successfulRes("Match created successfully", matchDTO));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> finishSingleMatch() throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userInfo.getUsername());
            if (userModel.getStatus() == 0) {
                return ResponseEntity.badRequest().body(ResponseUtils.unsuccessfulRes("You aren't enrolled in any matches currently.", null));
            }
            UserMatch userMatch = userMatchRepository.fetchUserMatchByUsername(userModel.getUsername());
            if (userMatch.getStatus() == 0) {
                userMatch.setStatus(3);
            }
            userModel.setStatus(0);
            userMatch.setState(1);
            userRepository.save(userModel);
            MatchDTO matchDTO = new MatchDTO();
            matchDTO.setMatchId(userMatch.getMatch().getMatchId());
            matchDTO.setStatus(matchStatusMapper.getStatusIntToString().get(userMatch.getStatus()));
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.successfulRes("Match finished successfully", matchDTO));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
