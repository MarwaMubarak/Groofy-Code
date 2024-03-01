package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.model.ClanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.groofycode.GroofyCode.service.ClanService;

@RestController
@RequestMapping("/clans")
public class ClanController {

    @Autowired
    private ClanService clanService;

    @PostMapping
    public ResponseEntity<?> createClan(@RequestBody ClanDTO clanDTO) {
        try {
            ClanDTO clan = clanService.createClan(clanDTO);
            return ResponseEntity.ok(clan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllClans() {
        return ResponseEntity.ok(clanService.getAllClans());
    }

    @GetMapping("/{clanId}")
    public ResponseEntity<?> getClanById(@PathVariable Long clanId) {
        try {
            ClanDTO clan = clanService.getClanById(clanId);
            return ResponseEntity.ok(clan);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{clanId}")
    public ResponseEntity<?> updateClan(@PathVariable Long clanId, @RequestBody ClanDTO clanDTO) {
        try {
            ClanDTO updatedClan = clanService.updateClan(clanId, clanDTO);
            return ResponseEntity.ok(updatedClan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{clanId}")
    public ResponseEntity<?> deleteClan(@PathVariable Long clanId) {
        try {
            clanService.deleteClan(clanId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/join/{clanId}")
    public ResponseEntity<?> joinClan(@PathVariable Long clanId, @RequestBody UserDTO memberDTO) {
        try {
            ClanDTO updatedClan = clanService.joinClan(clanId, memberDTO);
            return ResponseEntity.ok(updatedClan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/leave/{clanId}")
    public ResponseEntity<?> leaveClan(@PathVariable Long clanId, @RequestBody UserDTO memberDTO) {
        try {
            ClanDTO updatedClan = clanService.leaveClan(clanId, memberDTO);
            return ResponseEntity.ok(updatedClan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
