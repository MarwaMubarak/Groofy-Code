package com.groofycode.GroofyCode.utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter

public class ResponseModel<T> {
    private String status;
    private String message;
    private T data;

    public ResponseModel(String status, String message, T body) {
        this.status = status;
        this.message = message;
        this.data = body;
    }


    // Getters and setters
}