package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.ClanDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.groofycode.GroofyCode.service.ClanService;

@RestController
@RequestMapping("/clans")
public class ClanController {
    private final ClanService clanService;

    @Autowired
    public ClanController(ClanService clanService) {
        this.clanService = clanService;
    }

    @PostMapping
    public ResponseEntity<Object> createClan(@Valid @RequestBody ClanDTO clanDTO) throws Exception {
        return clanService.create(clanDTO);
    }

    @GetMapping
    public ResponseEntity<Object> getAllClans() throws Exception {
        return clanService.getAll();
    }

    @GetMapping("/{clanId}")
    public ResponseEntity<Object> getClanById(@PathVariable Long clanId) throws Exception {
        return clanService.getById(clanId);
    }

    @PutMapping("/{clanId}")
    public ResponseEntity<Object> updateClanName(@PathVariable Long clanId, @RequestBody ClanDTO clanDTO) throws Exception {
        return clanService.updateName(clanId, clanDTO.getName());
    }

    @DeleteMapping("/{clanId}")
    public ResponseEntity<Object> deleteClan(@PathVariable Long clanId) throws Exception {
        return clanService.delete(clanId);
    }

    @PutMapping("/join/{clanId}")
    public ResponseEntity<Object> joinClan(@PathVariable Long clanId) throws Exception {
        return clanService.joinClan(clanId);
    }

    @PutMapping("/leave/{clanId}")
    public ResponseEntity<?> leaveClan(@PathVariable Long clanId) throws Exception {
        return clanService.leaveClan(clanId);
    }
}
