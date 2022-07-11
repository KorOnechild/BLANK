package com.project.cafesns.error.exceptions.user;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyExistCafeException extends RuntimeException{

    private final ErrorCode errorCode;

    public AlreadyExistCafeException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
