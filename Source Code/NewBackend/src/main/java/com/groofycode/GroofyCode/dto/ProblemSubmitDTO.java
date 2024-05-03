package com.groofycode.GroofyCode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProblemSubmitDTO {
    String problemUrl;
    String language;
    String code;
}
