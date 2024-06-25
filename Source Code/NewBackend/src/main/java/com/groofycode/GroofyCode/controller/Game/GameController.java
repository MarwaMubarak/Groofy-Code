package com.groofycode.GroofyCode.controller.Game;


import com.groofycode.GroofyCode.dto.Game.ProblemSubmitDTO;
import com.groofycode.GroofyCode.dto.Game.RankedMatchDTO;
import com.groofycode.GroofyCode.dto.ProblemPickerDTO;
import com.groofycode.GroofyCode.model.Game.CasualMatch;
import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Game.RankedMatch;
import com.groofycode.GroofyCode.model.Game.SoloMatch;
import com.groofycode.GroofyCode.model.User.UserModel;
import com.groofycode.GroofyCode.service.Game.GameService;
import com.groofycode.GroofyCode.utilities.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<Object> getAllGames(@RequestParam(value = "p", required = false) Integer page) throws Exception{
        return gameService.findAllGames(page);
    }

    @PostMapping("/solo")
    public ResponseEntity<Object> createSoloMatch() {
        return gameService.findSoloMatch();
    }

//    @PostMapping("/ranked")
//    public Game createRankedMatch(@RequestParam String player1, @RequestParam String player2) {
//        RankedMatch rankedMatch = new RankedMatch(player1, player2, LocalDateTime.now());
//        return gameService.saveGame(rankedMatch);
//    }
//
//    @PostMapping("/casual")
//    public Game createCasualMatch(@RequestParam String player1, @RequestParam String player2) {
//        CasualMatch casualMatch = new CasualMatch(player1, player2, LocalDateTime.now());
//        return gameService.saveGame(casualMatch);
//    }


    @PostMapping("/ranked")
    public ResponseEntity<Object> findRankedMatch() {
        ResponseEntity<Object> match = gameService.findRankedMatch();
        if (match != null) {
            return match;
        } else {
            return ResponseEntity.ok(ResponseUtils.successfulRes("Still looking for a match", null)); // Still looking for a match
        }
    }

    @PostMapping("/leaveRankedQueue")
    public ResponseEntity<Object> leaveQueue() {
        return gameService.leaveRankedQueue();
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<Object> getMatch(@PathVariable Long matchId) {
        return gameService.getMatch(matchId);
    }


    @PutMapping("/{gameId}/leave")
    public ResponseEntity<Object> leaveMatch(@PathVariable Long gameId) {
        return gameService.leaveMatch(gameId);
    }

    @PostMapping("/{gameId}/submit")
    public ResponseEntity<Object> submitCode(@PathVariable Long gameId, @RequestBody ProblemSubmitDTO problemSubmitDTO) throws Exception {
        return gameService.submitCode(gameId, problemSubmitDTO);
    }

//    @PostMapping("/join")
//    public ResponseEntity<Void> joinGame(@RequestBody UserModel user) {
//        gameService.joinGame(user);
//        return ResponseEntity.ok().build();
//    }

}

////ToDo: Implement the joinGame method in the GameService class
////Todo: Edit the GameController class to use UserModel instead of String for player1 and player2