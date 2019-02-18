package com.cloud.serverusersecurity.model;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public class ResponseResult {

    private Integer status;

    private String message;

    private ResponseResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseResult build(Integer status, String message){
        return new ResponseResult(status,message);
    }

    public static ResponseResult build(String message){
        return new ResponseResult(HttpServletResponse.SC_OK,message);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
