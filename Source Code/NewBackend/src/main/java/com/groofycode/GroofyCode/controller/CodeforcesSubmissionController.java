//package com.groofycode.GroofyCode.controller;
//
//import com.groofycode.GroofyCode.service.CodeforcesSubmissionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class CodeforcesSubmissionController {
//    private final CodeforcesSubmissionService codeforcesSubmissionService;
//
//    @Autowired
//    public CodeforcesSubmissionController(CodeforcesSubmissionService codeforcesSubmissionService) {
//        this.codeforcesSubmissionService = codeforcesSubmissionService;
//    }
//
//    @PostMapping("/submit-code")
//    public void submitCode() {
//        codeforcesSubmissionService.submitCode("https://codeforces.com/contest/4/problem/A", "GNU G++17 7.3.0", "#include <bits/stdc++.h>\n" +
//                "using namespace std;\n" +
//                " \n" +
//                "int main() {\n" +
//                "    int x; cin >> x; cout << (x & 1 || x == 2 ? \"NO\\n\" : \"YES\\n\");\n" +
//                "}");
//    }
//}
