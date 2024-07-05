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

    @GetMapping("/pagination")
    public ResponseEntity<Object> getTeamsPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return teamService.getTeamsPage(page, size);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Object> getTeamInfo(@PathVariable Long teamId) {
        return teamService.getTeamInfo(teamId);
    }

    @GetMapping("/name/{teamName}")
    public ResponseEntity<Object> getTeamInfo(@PathVariable String teamName) {
        return teamService.getTeamInfo(teamName);
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

    @GetMapping("/search")
    public ResponseEntity<Object> getTeamsByPrefix(@RequestParam String prefix) {
        return teamService.getTeamsByPrefix(prefix);
    }

    @GetMapping("/my-teams")
    public ResponseEntity<Object> getTeamsByCreator(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return teamService.getTeamsByCreator(page, size);
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Object> getTeamsByPrefixWithPagination(@RequestParam String prefix,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size, Long team1ID) {
        return teamService.getTeamsByPrefixWithPagination(prefix, page, size, team1ID);
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

    @GetMapping("/search/to-invite")
    public ResponseEntity<Object> searchForFriendsByPrefixToInvite(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String prefix,
            @RequestParam Long teamID) throws Exception {

        return teamService.searchForFriendsByPrefix(page, size, prefix, teamID);
    }

}
