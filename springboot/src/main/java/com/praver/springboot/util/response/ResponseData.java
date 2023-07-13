package com.praver.springboot.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseData<K> {

    private Boolean success;
    private String message;
    private String code;
    private K data;

    public ResponseData(Boolean success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }
}
