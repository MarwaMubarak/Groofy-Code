package com.groofycode.GroofyCode.controller.Clan;

import com.groofycode.GroofyCode.dto.Clan.ClanDTO;
import com.groofycode.GroofyCode.dto.Clan.ClanRequestActionDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.groofycode.GroofyCode.service.ClanService;

import javax.annotation.Nullable;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/clans")
public class ClanController {
    private final ClanService clanService;

    @Autowired
    public ClanController(ClanService clanService) {
        this.clanService = clanService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllClans(@RequestParam("p") @Nullable Integer page, @RequestParam("s") @Nullable Integer size) throws Exception {
        return clanService.getAll(page, size);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getClanById() throws Exception {
        return clanService.getClan();
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> getClanRequests(@RequestParam("p") @Nullable Integer page, @RequestParam("s") @Nullable Integer size) throws Exception {
        return clanService.getClanRequests(page, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchClans(@RequestParam("name") String name, @RequestParam("p") @Nullable Integer page,
                                              @RequestParam("s") @Nullable Integer size) throws Exception {
        return clanService.searchForClan(name, page, size);
    }

    @PostMapping
    public ResponseEntity<Object> createClan(@Valid @RequestBody ClanDTO clanDTO) throws Exception {
        return clanService.create(clanDTO);
    }

    @PutMapping("/{clanId}")
    public ResponseEntity<Object> updateClanName(@PathVariable Long clanId, @RequestBody ClanDTO clanDTO) throws Exception {
        return clanService.updateName(clanId, clanDTO.getName());
    }

    @DeleteMapping("/{clanId}")
    public ResponseEntity<Object> deleteClan(@PathVariable Long clanId) throws Exception {
        return clanService.delete(clanId);
    }

    @PostMapping("/request/{clanId}")
    public ResponseEntity<Object> ClanRequest(@PathVariable Long clanId) throws Exception {
        return clanService.clanRequest(clanId);
    }

    @PostMapping("/requests")
    public ResponseEntity<Object> ClanRequestAction(@RequestBody ClanRequestActionDTO clanRequestActionDTO) throws Exception {
        return clanService.clanRequestAction(clanRequestActionDTO);
    }

    @PostMapping("/leave")
    public ResponseEntity<Object> leaveClan() throws Exception {
        return clanService.leaveClan();
    }
}
