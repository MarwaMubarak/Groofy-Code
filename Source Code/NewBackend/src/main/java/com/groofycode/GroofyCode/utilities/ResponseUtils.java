package com.groofycode.GroofyCode.utilities;

public class ResponseUtils {
    public static <T> ResponseModel<T> successfulRes(String massage, T body) {
        if (body == null) {
            body = (T) "No data exist!";
        }
        return new ResponseModel<>("success", massage, body);
    }

    public static <T> ResponseModel<T> unsuccessfulRes(String massage, T body) {
        if (body == null) {
            body = (T) "No data exist!";
        }
        return new ResponseModel<>("failure", massage, body);
    }
}
