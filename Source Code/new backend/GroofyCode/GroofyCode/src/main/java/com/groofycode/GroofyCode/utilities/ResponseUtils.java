package com.groofycode.GroofyCode.utilities;

import org.springframework.http.HttpStatus;

public class ResponseUtils {
    public static <T> ResponseModel<T> successfulRes(HttpStatus statusHttp, String massage, T data) {
        return new ResponseModel<>(statusHttp,"success", massage, data);
    }

    public static <T> ResponseModel<T> unsuccessfulRes(HttpStatus statusHttp, String massage, T data) {
        if (data == null) {
            data = (T) "No data exist!";
        }
        return new ResponseModel<>(statusHttp,"failure", massage, data);
    }
}
