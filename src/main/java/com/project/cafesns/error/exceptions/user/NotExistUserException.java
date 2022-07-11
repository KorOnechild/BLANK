package com.project.cafesns.error.exceptions.user;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotExistUserException extends RuntimeException{

    private final ErrorCode errorCode;

    public NotExistUserException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
