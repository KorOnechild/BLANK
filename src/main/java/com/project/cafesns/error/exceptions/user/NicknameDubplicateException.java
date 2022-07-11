package com.project.cafesns.error.exceptions.user;

import com.project.cafesns.error.ErrorCode;
import lombok.Getter;

@Getter
public class NicknameDubplicateException extends RuntimeException{
    private final ErrorCode errorCode;

    public NicknameDubplicateException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
