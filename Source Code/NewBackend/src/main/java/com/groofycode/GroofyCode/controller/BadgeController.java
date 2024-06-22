package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.BadgeDTO;
import com.groofycode.GroofyCode.service.BadgeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/badges")
public class BadgeController {
    private final BadgeService badgeService;

    @Autowired
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllBadges() throws Exception {
        return badgeService.getAllBadges();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBadgeById(@PathVariable Long id) throws Exception {
        return badgeService.getBadgeById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createBadge(@Valid @RequestBody BadgeDTO badgeDTO) throws Exception {
        return badgeService.createBadge(badgeDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBadge(@PathVariable Long id, @Valid @RequestBody BadgeDTO badgeDTO) throws Exception {
        return badgeService.updateBadge(id, badgeDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBadge(@PathVariable Long id) throws Exception {

        return badgeService.deleteBadge(id);
    }
}