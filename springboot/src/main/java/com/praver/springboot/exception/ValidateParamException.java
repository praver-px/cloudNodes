package com.praver.springboot.exception;


import lombok.Getter;

@Getter
public class ValidateParamException extends ServiceException{
    public ValidateParamException(String message, String code) {
        super(message, code);
    }
}
