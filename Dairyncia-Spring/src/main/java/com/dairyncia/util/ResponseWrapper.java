package com.dairyncia.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
    private T data;
    private String message;
    private Integer statusCode;
    private Boolean success;

    public static <T> ResponseWrapper<T> success(T data) {
        ResponseWrapper<T> response = new ResponseWrapper<>();
        response.setData(data);
        response.setSuccess(true);
        response.setStatusCode(200);
        return response;
    }

    public static <T> ResponseWrapper<T> success(T data, String message) {
        ResponseWrapper<T> response = new ResponseWrapper<>();
        response.setData(data);
        response.setMessage(message);
        response.setSuccess(true);
        response.setStatusCode(200);
        return response;
    }

    public static <T> ResponseWrapper<T> error(String message, Integer statusCode) {
        ResponseWrapper<T> response = new ResponseWrapper<>();
        response.setMessage(message);
        response.setSuccess(false);
        response.setStatusCode(statusCode);
        return response;
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return error(message, 400);
    }
}