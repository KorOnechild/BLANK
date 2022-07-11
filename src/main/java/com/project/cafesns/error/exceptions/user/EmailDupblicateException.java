package com.project.cafesns.error.exceptions.user;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class EmailDupblicateException extends RuntimeException{

    private final ErrorCode errorCode;

    public EmailDupblicateException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
