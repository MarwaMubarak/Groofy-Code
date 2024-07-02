package com.groofycode.GroofyCode.controller.Game;

import com.groofycode.GroofyCode.service.Game.MatchInvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game/beat-friend")
public class BeatAFriendController {

    @Autowired
    private MatchInvitationService beatAFriendService;

    @PostMapping("/invite/{receiverUserId}")
    public ResponseEntity<Object> sendInvitation(@PathVariable Long receiverUserId) throws Exception {
        return beatAFriendService.sendFriendlyMatchInvitation(receiverUserId);
    }

    @PostMapping("/acceptInvitation")
    public ResponseEntity<Object> acceptInvitation(@RequestParam Long invitationId) {
        return beatAFriendService.acceptFriendMatchInvitation(invitationId);
    }

    @PostMapping("/rejectInvitation")
    public ResponseEntity<Object> rejectInvitation(@RequestParam Long invitationId) {
        return beatAFriendService.rejectFriendMatchInvitation(invitationId);
    }

    @PostMapping("/cancelInvitation/{invitationId}")
    public ResponseEntity<Object> cancelInvitation(@PathVariable Long invitationId) {
        return beatAFriendService.cancelFriendMatchInvitation(invitationId);
    }
}
