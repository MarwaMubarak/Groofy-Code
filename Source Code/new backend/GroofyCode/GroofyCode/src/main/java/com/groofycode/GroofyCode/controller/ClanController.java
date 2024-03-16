package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import com.groofycode.GroofyCode.service.ClanService;

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
    public ResponseEntity<?> joinClan(@PathVariable Long clanId) {
       return clanService.joinClan(clanId);
    }

    @PutMapping("/leave/{clanId}")
    public ResponseEntity<?> leaveClan(@PathVariable Long clanId) {
       return clanService.leaveClan(clanId);
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleValidationExceptions(BindException ex) {
       String errors = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toString();
        return  ResponseEntity.status( HttpStatus.BAD_REQUEST).body(ResponseUtils.unsuccessfulRes(errors,null));
    }

}
