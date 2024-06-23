package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.utilities.ProblemFetcher;
import com.groofycode.GroofyCode.utilities.ProblemParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Vector;

@RestController
public class ProblemController {
    private final ProblemParser problemParser;
    private final ProblemFetcher problemFetcher;

    @Autowired
    public ProblemController(ProblemParser problemParser, ProblemFetcher problemFetcher) {
        this.problemParser = problemParser;
        this.problemFetcher = problemFetcher;
    }

    @GetMapping("/problemset/problem/{contestId}/{problemId}")
    public ResponseEntity<Object> testProblem(@PathVariable Integer contestId, @PathVariable String problemId) throws Exception{
//        Vector<String> ret = problemParser.parseSolvedProblemCount("https://codeforces.com/contest/1978");
        return problemParser.parseFullProblem(contestId, problemId);
    }
}
