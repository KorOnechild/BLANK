package com.project.cafesns.error.exceptions;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotmatchKeyException extends RuntimeException {
    private ErrorCode errorCode;

    public NotmatchKeyException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
