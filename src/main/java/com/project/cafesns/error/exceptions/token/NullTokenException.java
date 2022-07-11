package com.project.cafesns.error.exceptions.token;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NullTokenException extends RuntimeException{

    private final ErrorCode errorCode;

    public NullTokenException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
