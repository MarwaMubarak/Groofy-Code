package com.groofycode.GroofyCode.controller;

import com.groofycode.GroofyCode.service.CodeforcesSubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
@SecurityRequirement(name = "bearerAuth")
@RestController
public class CodeforcesSubmissionController {
    private final CodeforcesSubmissionService codeforcesSubmissionService;

    @Autowired
    public CodeforcesSubmissionController(CodeforcesSubmissionService codeforcesSubmissionService) {
        this.codeforcesSubmissionService = codeforcesSubmissionService;
    }

//    @PostMapping("/submit-code")
//    public ResponseEntity<Object> submitCode(@RequestBody ProblemSubmitDTO problemSubmitDTO) throws Exception {
//        return codeforcesSubmissionService.submitCode(problemSubmitDTO);
//    }
}

// https://codeforces.com/contest/4/problem/A
// GNU G++17 7.3.0
/*
                #include <bits/stdc++.h>
                using namespace std;
                int main() {
                    int x; cin >> x; cout << (x & 1 || x == 2 ? "NO\n" : "YES\n");
                }
*/