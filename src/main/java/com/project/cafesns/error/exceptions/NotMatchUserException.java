package com.project.cafesns.error.exceptions;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotMatchUserException extends RuntimeException{
    private ErrorCode errorCode;

    public NotMatchUserException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}