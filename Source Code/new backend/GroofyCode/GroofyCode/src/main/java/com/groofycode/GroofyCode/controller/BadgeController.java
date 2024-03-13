package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/badges")
public class BadgeController {
    @Autowired
    private BadgeService badgeService;

    @GetMapping
    public ResponseEntity<List<BadgeDTO>> getAllBadges() {
        List<BadgeDTO> badges = badgeService.getAllBadges();
        return new ResponseEntity<>(badges, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeDTO> getBadgeById(@PathVariable Long id) {
        BadgeDTO badge = badgeService.getBadgeById(id);
        if (badge != null) {
            return new ResponseEntity<>(badge, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<BadgeDTO> createBadge(@RequestBody @Valid BadgeDTO badgeDTO) {
        BadgeDTO createdBadge = badgeService.createBadge(badgeDTO);
        return new ResponseEntity<>(createdBadge, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBadge(@PathVariable Long id) {
        badgeService.deleteBadge(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
