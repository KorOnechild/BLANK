package com.project.cafesns.error.exceptions.token;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotExistTokenException extends RuntimeException{

    private final ErrorCode errorCode;

    public NotExistTokenException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
