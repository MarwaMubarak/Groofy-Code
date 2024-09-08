package com.groofycode.GroofyCode.utilities;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
public class MatchStatusMapper {
    private final HashMap<Integer, String> StatusIntToString = new HashMap<>();

    private final HashMap<String, Integer> StatusStringToInt = new HashMap<>();

    private final HashMap<Integer,String> StateIntToString = new HashMap<>();

    public MatchStatusMapper() {
        StatusIntToString.put(0, "None");
        StatusIntToString.put(1, "Accepted");
        StatusIntToString.put(2, "Partial");
        StatusIntToString.put(3, "Wrong Answer");
        StatusIntToString.put(4, "Time Limit Exceeded");
        StatusIntToString.put(5, "Memory Limit Exceeded");
        StatusIntToString.put(6, "Runtime Error");
        StatusIntToString.put(7, "Compilation Error");
        StatusIntToString.put(8, "Presentation Error");
        StatusIntToString.put(9, "Failed");

        StateIntToString.put(0, "On Going");
        StateIntToString.put(1, "Finished");

        StatusStringToInt.put("None", 0);
        StatusStringToInt.put("OK", 1);
        StatusStringToInt.put("PARTIAL", 2);
        StatusStringToInt.put("WRONG_ANSWER", 3);
        StatusStringToInt.put("TIME_LIMIT_EXCEEDED", 4);
        StatusStringToInt.put("MEMORY_LIMIT_EXCEEDED", 5);
        StatusStringToInt.put("RUNTIME_ERROR", 6);
        StatusStringToInt.put("COMPILATION_ERROR", 7);
        StatusStringToInt.put("PRESENTATION_ERROR", 8);
        StatusStringToInt.put("FAILED", 9);
    }
}
