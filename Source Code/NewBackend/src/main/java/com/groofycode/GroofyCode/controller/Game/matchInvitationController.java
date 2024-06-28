package com.groofycode.GroofyCode.controller.Game;

import com.groofycode.GroofyCode.service.Game.MatchInvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
public class matchInvitationController {

    @Autowired
    private MatchInvitationService matchInvitationService;

    @PostMapping("/invite")
    public ResponseEntity<Object> sendInvitation(@RequestParam Long gameId, @RequestParam String receiverUsername) {
        return matchInvitationService.sendMatchInvitation(gameId, receiverUsername);
    }

    @PostMapping("/acceptInvitation")
    public ResponseEntity<Object> acceptInvitation(@RequestParam Long invitationId) {
        return matchInvitationService.acceptMatchInvitation(invitationId);
    }

    @PostMapping("/rejectInvitation")
    public ResponseEntity<Object> rejectInvitation(@RequestParam Long invitationId) {
        return matchInvitationService.rejectMatchInvitation(invitationId);
    }

    @PostMapping("/cancelInvitation")
    public ResponseEntity<Object> cancelInvitation(@RequestParam Long invitationId) {
        return matchInvitationService.cancelMatchInvitation(invitationId);
    }
}
