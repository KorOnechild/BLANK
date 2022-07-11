package com.project.cafesns.error.exceptions.user;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotmatchUserException extends RuntimeException {
    private ErrorCode errorCode;

    public NotmatchUserException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
