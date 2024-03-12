package com.groofycode.GroofyCode.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter

public class ResponseModel<T> {
    private HttpStatus statusHttp;
    private String status;
    private String message;
    private T body;

    public ResponseModel(HttpStatus statusHttp, String status, String message, T body) {
        this.statusHttp = statusHttp;
        this.status = status;
        this.message = message;
        this.body = body;
    }


    // Getters and setters
}