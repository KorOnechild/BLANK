package com.project.cafesns.error.exceptions.token;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class BearerTokenException extends RuntimeException{
    private final ErrorCode errorCode;

    public BearerTokenException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
