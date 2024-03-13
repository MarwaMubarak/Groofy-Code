package com.groofycode.GroofyCode.utilities;

import org.springframework.http.HttpStatus;

public class ResponseUtils {
    public static <T> ResponseModel<T> successfulRes( String massage, T data) {
        return new ResponseModel<>("success", massage, data);
    }

    public static <T> ResponseModel<T> unsuccessfulRes( String massage, T data) {
        if (data == null) {
            data = (T) "No data exist!";
        }
        return new ResponseModel<>("failure", massage, data);
    }
}
