package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.model.ClanModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import com.groofycode.GroofyCode.service.ClanService;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clans")
public class ClanController {

    @Autowired
    private ClanService clanService;

    @PostMapping
    public ResponseEntity<?> createClan(@Valid @RequestBody ClanDTO clanDTO) {
        return clanService.create(clanDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllClans() {
        return clanService.getAll();
    }

    @GetMapping("/{clanId}")
    public ResponseEntity<?> getClanById(@PathVariable Long clanId) {
       return clanService.getById(clanId);
    }

    @PutMapping("/{clanId}")
    public ResponseEntity<?> updateClanName(@PathVariable Long clanId, @RequestBody ClanDTO clanDTO) {
      return clanService.updateName(clanId,clanDTO.getName());
    }

    @DeleteMapping("/{clanId}")
    public ResponseEntity<?> deleteClan(@PathVariable Long clanId) {
        return clanService.delete(clanId);
    }

    @PutMapping("/join/{clanId}")
    public ResponseEntity<?> joinClan(@PathVariable Long clanId, @RequestBody Long memberId) {
       return clanService.joinClan(clanId,memberId);
    }

    @PutMapping("/leave/{clanId}")
    public ResponseEntity<?> leaveClan(@PathVariable Long clanId, @RequestBody Long memberId) {
       return clanService.leaveClan(clanId,memberId);
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<HashMap<String, List<String>>> handleValidationExceptions(BindException ex) {
        List<String>errors = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        HashMap<String,List<String>>errMap  = new HashMap<>();
        errMap.put("errors",errors);
        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }

}
