package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.dto.PlayerDTO;
import com.groofycode.GroofyCode.dto.ProblemDTO;
import com.groofycode.GroofyCode.dto.ProblemPickerDTO;
import com.groofycode.GroofyCode.service.ProblemPicker;
import com.groofycode.GroofyCode.utilities.ProblemFetcher;
import com.groofycode.GroofyCode.utilities.ProblemParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ProblemController {
    private final ProblemParser problemParser;
    private final ProblemFetcher problemFetcher;
    private final ProblemPicker problemPicker;

    @Autowired
    public ProblemController(ProblemParser problemParser, ProblemFetcher problemFetcher, ProblemPicker problemPicker) {
        this.problemParser = problemParser;
        this.problemFetcher = problemFetcher;
        this.problemPicker = problemPicker;
    }

    @GetMapping("/problemset/problem/{contestId}/{problemId}")
    public ResponseEntity<Object> testProblem(@PathVariable Integer contestId, @PathVariable String problemId) throws Exception{
        return problemParser.parseFullProblem(contestId, problemId);
    }

    @GetMapping("/problemset/problem/fetch")
    public ResponseEntity<Object> fetchProblems() throws Exception{
        return problemFetcher.fetchProblems();
    }

    @PostMapping("/problemset/problem/pick")
    public ResponseEntity<Object> pickProblem(@RequestBody ProblemPickerDTO problemPickerDTO) throws Exception{
        return problemPicker.pickProblem(problemPickerDTO.getPlayer(), problemPickerDTO.getSolvedProblems());
    }
}
