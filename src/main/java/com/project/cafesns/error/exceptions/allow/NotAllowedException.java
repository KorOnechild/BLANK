package com.project.cafesns.error.exceptions.allow;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotAllowedException extends RuntimeException{
    private final ErrorCode errorCode;

    public NotAllowedException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
