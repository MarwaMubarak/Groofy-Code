package com.groofycode.GroofyCode.controller.Match;

import com.groofycode.GroofyCode.service.Match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<Object> GetUserMatches(@RequestParam("p") Integer page, @RequestParam("s") Integer size) throws Exception {
        return matchService.getUserMatches(page,size);
    }

    @GetMapping("/current")
    public ResponseEntity<Object> GetCurrentMatch() throws Exception {
        return matchService.getCurrentMatch();
    }

    @PostMapping("/solo-match")
    public ResponseEntity<Object> CreateSoloMatch() throws Exception {
        return matchService.createSoloMatch();
    }

    @PostMapping("/finish-match")
    public ResponseEntity<Object> FinishCurrentMatch() throws Exception {
        return matchService.finishSingleMatch();
    }
}
