package com.groofycode.GroofyCode.controller.Game;


import com.groofycode.GroofyCode.model.Game.CasualMatch;
import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Game.RankedMatch;
import com.groofycode.GroofyCode.model.Game.SoloMatch;
import com.groofycode.GroofyCode.service.Game.GameService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.findAllGames();
    }

    @PostMapping("/solo")
    public Game createSoloMatch(@RequestParam String player1) {
        SoloMatch soloMatch = new SoloMatch(player1, LocalDateTime.now());
        return gameService.saveGame(soloMatch);
    }

    @PostMapping("/ranked")
    public Game createRankedMatch(@RequestParam String player1, @RequestParam String player2) {
        RankedMatch rankedMatch = new RankedMatch(player1, player2, LocalDateTime.now());
        return gameService.saveGame(rankedMatch);
    }

    @PostMapping("/casual")
    public Game createCasualMatch(@RequestParam String player1, @RequestParam String player2) {
        CasualMatch casualMatch = new CasualMatch(player1, player2, LocalDateTime.now());
        return gameService.saveGame(casualMatch);
    }

}