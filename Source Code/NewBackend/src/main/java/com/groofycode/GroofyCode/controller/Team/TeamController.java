package com.groofycode.GroofyCode.controller.Team;

import com.groofycode.GroofyCode.dto.Team.TeamDTO;
import com.groofycode.GroofyCode.service.Team.TeamService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTeams() {
        return teamService.getAll();
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Object> getTeamInfo(@PathVariable Long teamId) {
        return teamService.getTeamInfo(teamId);
    }
    @GetMapping("/invitations")
    public ResponseEntity<Object> getUserInvitations() {
        return teamService.getUserInvitations();
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createTeam(@RequestBody TeamDTO teamDTO) {
        return teamService.create(teamDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getTeamsByUser() {
        return teamService.getTeamsByUser();
    }

    @PostMapping("/invite")
    public ResponseEntity<Object> sendInvitation(@RequestParam Long teamId, @RequestParam String receiverUsername) {
        return teamService.teamInvitation(teamId, receiverUsername);
    }

    @PostMapping("/acceptInvitation")
    public ResponseEntity<Object> acceptInvitation(@RequestParam Long invitationId) {
        return teamService.acceptInvitation(invitationId);
    }

    @PostMapping("/rejectInvitation")
    public ResponseEntity<Object> rejectInvitation(@RequestParam Long invitationId) {
        return teamService.rejectInvitation(invitationId);
    }

    @PostMapping("/cancelInvitation")
    public ResponseEntity<Object> cancelInvitation(@RequestParam Long invitationId) {
        return teamService.cancelInvitation(invitationId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTeam(@RequestParam Long teamId) {
        return teamService.deleteTeam(teamId);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Object> leaveTeam(@RequestParam Long teamId) {
        return teamService.leaveTeam(teamId);
    }

    @DeleteMapping("/removeMember")
    public ResponseEntity<Object> removeMember(@RequestParam Long teamId, @RequestParam String memberUsername) {
        return teamService.removeMember(teamId, memberUsername);
    }

    @PutMapping("/updateName")
    public ResponseEntity<Object> updateTeamName(@RequestParam Long teamId, @RequestParam String newTeamName) {
        return teamService.updateTeamName(teamId, newTeamName);
    }

}
