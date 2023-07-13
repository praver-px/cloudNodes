package com.praver.springboot.exception;


import lombok.Getter;

@Getter
public class ServiceException extends Exception{
    private String code;//业务状态码

    public ServiceException(String message, String code) {
        super(message);
        this.code = code;
    }
}
