package com.groofycode.GroofyCode.utilities;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RatingSystemCalculator {

    private final int adjustmentA = 10;
    private final int adjustmentB = 20;
    private final int adjustmentC = 30;
    private final Map<Character, Double> ratingSystem = new HashMap<>();

    RatingSystemCalculator() {
        ratingSystem.put('W', 1.0);
        ratingSystem.put('D', 0.5);
        ratingSystem.put('L', 0.0);
    }
    // differenceRating = Opponent Current Rating - My Current Rating
    // If I have two players RatingA = 2100, RatingB = 2300
    // And I want to calculate the delta for player A.
    // DeltaA = calculateDeltaRating(RatingB - RatingA, RatingA, 'W')
    // DeltaB = calculateDeltaRating(RatingA - RatingB, RatingB, 'L')

    int calculateDeltaRating(int differenceRating, int currentRating, char matchStatus) {
        double EloPlayerRating = 1.0 / (1 + Math.pow(10, (double) differenceRating / 400));
        if(currentRating >= 2400){
            return (int) (currentRating + adjustmentA * (ratingSystem.get(matchStatus) - EloPlayerRating));
        }
        else if(currentRating >= 2100){
            return (int) (currentRating + adjustmentB * (ratingSystem.get(matchStatus) - EloPlayerRating));
        }
        else{
            return (int) (currentRating + adjustmentC * (ratingSystem.get(matchStatus) - EloPlayerRating));
        }
    }
}
