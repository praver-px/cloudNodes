package com.praver.springboot.exception;


import lombok.Getter;

@Getter
public class ServiceRollbackException extends ServiceException{
    public ServiceRollbackException(String message, String code) {
        super(message, code);
    }
}
