package com.groofycode.GroofyCode.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemSubmitDTO {
    String username;
    String password;
    String problemUrl;
    String language;
    String code;
}
