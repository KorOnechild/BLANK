package com.project.cafesns.error.exceptions.register;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyExistRegistedException extends RuntimeException{
    private final ErrorCode errorCode;

    public AlreadyExistRegistedException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
