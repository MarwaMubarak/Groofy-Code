package com.groofycode.GroofyCode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayerDTO {
    Long id;
    Integer expectedRatingL;
    Integer expectedRatingR;
}
