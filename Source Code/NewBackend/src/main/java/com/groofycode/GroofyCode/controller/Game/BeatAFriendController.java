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

    @PostMapping("/invite")
    public ResponseEntity<Object> sendInvitation(@RequestParam String receiverUsername) {
        return beatAFriendService.sendFriendMatchInvitation(receiverUsername);
    }

    @PostMapping("/acceptInvitation")
    public ResponseEntity<Object> acceptInvitation(@RequestParam Long invitationId) {
        return beatAFriendService.acceptFriendMatchInvitation(invitationId);
    }

    @PostMapping("/rejectInvitation")
    public ResponseEntity<Object> rejectInvitation(@RequestParam Long invitationId) {
        return beatAFriendService.rejectFriendMatchInvitation(invitationId);
    }

    @PostMapping("/cancelInvitation")
    public ResponseEntity<Object> cancelInvitation(@RequestParam Long invitationId) {
        return beatAFriendService.cancelFriendMatchInvitation(invitationId);
    }
}
